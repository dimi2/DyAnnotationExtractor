# DyAnnotationExtractor #

DyAnnotationExtractor is software for extracting annotations (highlighted text and comments) from e-documents like PDF. The extracted parts can be used to build summary/resume of the document.

## Usage ##

Imagine you have ebook (PDF) which is 100 pages long. While reading the book, 
you **highlight** the important parts in your favorite reader:

![](work/documents/Highlight_Example_1.png)

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

Get the [latest release](https://github.com/dimi2/DyAnnotationExtractor/releases/latest).

There are separate files for: distribution, binary and sources.<br/>
End users need to download only the distribution.

## Installation ##

Extract the downloaded archive in some local directory.<br/>
Run the provided 'DyAnnotationExtractor' script to perform extraction.

## Dependencies ##

- iTextPdf 7.0.2+ (PDF handling library)

