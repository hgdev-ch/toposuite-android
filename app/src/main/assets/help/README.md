# HOW TO WRITE/UPDATE HELP CONTENT

The help files are written in reStructuredText and then converted to HTML.
`rst2html` from Python docutils works very nicely for this task:

    rst2html help.rst help.html
