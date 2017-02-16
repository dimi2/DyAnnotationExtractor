# DyAnnotationExtractor #

DyAnnotationExtractor is software for extracting annotations (highlighted text and comments) from e-documents like PDF. The extracted parts can be used to build summary/resume of the document.

## Usage ##

Imagine you have ebook (PDF) which is 100 pages long. While reading the book, 
you **highlight** the important parts in your favorite reader:

![](Highlight_Example_1.png)

Then use the DyAnnotationExtractor tool to get just the highlighted parts. 

Via the command line:
```
DyAnnotationExtractor -input "Getting Started with Ubuntu 16.04.pdf"
```

(this will create file with same name in the same directory, with added '.md' suffix)

Now you have extract of the book which is not 100 but 5-6 pages. You can skim just the exported text instead of re-reading entire book.

## Supported Input Formats ##

- PDF (Portable Document Format)

## Supported Output Formats ##

- MD (Markdown)

## Requirements ##

- Java 8+.

## Download ##

Get the [binary distribution](https://github.com/dyannotationextractor/anotex/releases/latest).

Get the [source code](https://github.com/dyannotationextractor/anotex/releases/latest).

TODO: curl command for download

## Installation ##

Extract the downloaded archive in some local directory.

TODO: shell command to extract

## License ##

This software is distributed under the terms of LGPL license.

TODO: Link to full license text

## Dependencies ##

- iTextPdf 7.0.1+ (PDF handling library)

