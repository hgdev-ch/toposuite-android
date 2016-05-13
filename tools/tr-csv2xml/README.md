# tr-csv2xml

tr-csv2xml converts a CSV file of translations into appropriate XML files.

CSV file must have the following format:

* First line contain columns labels
* Column 1 contains the translations labels
* Column 2 contains the english translation
* Column 3 contains the french translation
* Column 4 contains the german translation
* Column 5 contains the italian translation


## Dependencies

The only dependency is [Go](https://golang.org/)


## Build

To build this tool, issue the following command:

```
go build .
```

You may also simply run this program like a script.

## Usage

See

```
tr-csv2xml -h
```
