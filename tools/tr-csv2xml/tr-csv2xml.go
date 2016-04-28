//usr/bin/env go run $0 $@; exit
//
// Copyright 2015 The project AUTHORS. All rights reserved.
// Use of this source code is governed by a BSD-style
// license that can be found in the LICENSE file.
//
// tr-csv2xml converts a CSV file of translations into appropriate XML files.
//
// CSV file must have the following format:
//   * First line contain columns labels
//   * Column 1 contains the translations labels
//   * Column 2 contains the english translation
//   * Column 3 contains the french translation
//   * Column 4 contains the german translation
//   * Column 5 contains the italian translation
package main

import (
	"encoding/csv"
	"flag"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"strings"
	"text/template"
)

// output filename
const filename = "strings.xml"

// Language code for non-default language
const (
	LANG_FR = "fr" // French
	LANG_DE = "de" // German
	LANG_IT = "it" // Italian
)

const perm = 0755 // output directory permissions

// template for rendering Android XML translation file
const tmpl = `<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:xliff="urn:oasis:names:tc:xliff:document:1.2">
	{{ range $index, $value := . }}<string name="{{ $value.Name }}">{{ $value.Value }}</string>
	{{ end }}
</resources>`

// render templates
func render(name string, w io.Writer, data []attr) error {
	t, err := template.New(name).Parse(tmpl)
	if err != nil {
		return err
	}
	return t.Execute(w, data)
}

// escapeApostrophe escapes apostrophe signs from the string s
func escapeApostrophe(s string) string {
	return strings.Replace(s, "'", "\\'", -1)
}

// attr represents a XML attribute.
type attr struct {
	Name  string
	Value string
}

// fatal logs to stderr whatever is given as input and exit with status 1.
func fatal(a ...interface{}) {
	fmt.Fprintln(os.Stderr, a...)
	os.Exit(1)
}

// same as fatal, but with formatting options
func fatalf(format string, a ...interface{}) {
	fatal(fmt.Sprintf(format, a...))
}

// print usage
func usage() {
	fmt.Fprintf(os.Stdout, "Usage: %s [CSV FILE]\n", os.Args[0])
	flag.PrintDefaults()
}

// command line options
var (
	sep       = flag.String("s", ",", "CSV separator")
	outputDir = flag.String("o", "values", "Output folder")
)

func main() {
	flag.Usage = usage
	flag.Parse()

	if n := flag.NArg(); n != 1 {
		fatalf("invalid # of arguments: expected 1, found %d", n)
	}

	if len(*sep) != 1 {
		fatal("invalid separator")
	}

	if len(*outputDir) == 0 {
		fatal("output directory cannot be empty")
	}

	// per-lang directories
	var (
		dirEn = *outputDir
		dirFr = *outputDir + "-" + LANG_FR
		dirDe = *outputDir + "-" + LANG_DE
		dirIt = *outputDir + "-" + LANG_IT
	)

	// create output directories
	if err := os.MkdirAll(dirEn, perm); err != nil {
		fatal(err)
	}
	if err := os.MkdirAll(dirFr, perm); err != nil {
		fatal(err)
	}
	if err := os.MkdirAll(dirDe, perm); err != nil {
		fatal(err)
	}
	if err := os.MkdirAll(dirIt, perm); err != nil {
		fatal(err)
	}

	in, err := os.Open(flag.Arg(0))
	if err != nil {
		fatal(err)
	}
	defer in.Close()

	r := csv.NewReader(in)
	r.Comma = rune((*sep)[0])

	// read all CSV at once, since we know it will fit in memory
	records, err := r.ReadAll()
	if err != nil {
		fatal(err)
	}

	if len(records) <= 1 {
		fatal("empty input file")
	}

	// total entries (first line is not taken into account)
	numEntries := len(records) - 1

	// create list of translations for each language
	en := make([]attr, numEntries, numEntries)
	fr := make([]attr, numEntries, numEntries)
	de := make([]attr, numEntries, numEntries)
	it := make([]attr, numEntries, numEntries)

	for idx, entry := range records[1:] {
		if len(entry) != 5 {
			fatalf("malformed CSV entry at line %d", idx+2)
		}
		en[idx] = attr{Name: entry[0], Value: escapeApostrophe(entry[1])}
		fr[idx] = attr{Name: entry[0], Value: escapeApostrophe(entry[2])}
		de[idx] = attr{Name: entry[0], Value: escapeApostrophe(entry[3])}
		it[idx] = attr{Name: entry[0], Value: escapeApostrophe(entry[4])}
	}

	// render template for each language

	// render english translations
	outputEn, err := os.Create(filepath.Join(dirEn, filename))
	if err != nil {
		fatal(err)
	}
	defer outputEn.Close()

	if err := render(dirEn, outputEn, en); err != nil {
		fatal(err)
	}

	// render french translations
	outputFr, err := os.Create(filepath.Join(dirFr, filename))
	if err != nil {
		fatal(err)
	}
	defer outputFr.Close()

	if err := render(dirFr, outputFr, fr); err != nil {
		fatal(err)
	}

	// render german translations
	outputDe, err := os.Create(filepath.Join(dirDe, filename))
	if err != nil {
		fatal(err)
	}
	defer outputDe.Close()

	if err := render(dirDe, outputDe, de); err != nil {
		fatal(err)
	}

	// render italian translations
	outputIt, err := os.Create(filepath.Join(dirIt, filename))
	if err != nil {
		fatal(err)
	}
	defer outputIt.Close()

	if err := render(dirIt, outputIt, it); err != nil {
		fatal(err)
	}

	fmt.Println("Done.")
}
