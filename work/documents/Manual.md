# DyAnnotationExtractor #

DyAnnotationExtractor is software for extracting annotations (highlighted text and comments) from e-documents like PDF. The extracted parts can be used to build summary/resume of the document.

<span style="color:red">
Note! The AI bot [ChatGPT](https://chatgpt.com/) is now cable to extract highlighted text from PDF file and export the summary into Markdown. There is no stimulus to develop this project further. 
DyAnnotationExtractor remains usable in its current state - it can be applied in task automation tools which cannot call AI service (because of sensitive documents or internet restrictions).
</span>

## Usage ##

Imagine you have ebook (PDF) which is 100 pages long. While reading the book, 
you **highlight** the important parts in your favorite reader:

![](Highlight_Example_1.png)

Then use the DyAnnotationExtractor tool to get just the highlighted parts. 

Via the command line:
```console
DyAnnotationExtractor -input "Getting Started with Ubuntu 16.04.pdf"
```

This will create a file with same name in the same directory, with added '.md' suffix. 
Note that the file name is enclosed with quotas - this is required when the file name contains spaces.

Now you have extract of the book which is not 100 but 5-6 pages. So, you can skim just the exported text instead of re-reading the entire book.

## Supported Input Formats ##

- PDF (Portable Document Format)

## Supported Output Formats ##

- MD (Markdown)

## Requirements ##

- Java 21+.

## Download ##

Get the [latest release](https://github.com/dimi2/DyAnnotationExtractor/releases/latest).


End users need to download only the distribution jar.

## Installation ##

Extract the downloaded archive in some local directory.<br/>
Run the provided 'DyAnnotationExtractor' script to perform extraction.

## Build ##

To build the project from sources, you will need [Gradle](https://gradle.org/) build tool.
Go into the project home directory (PROJ_HOME) and execute command:

```
gradle
```
The result will appear in directory `PROJ_HOME/build/distribution`. This is portable distribution of the application. If you need just the library (without dependencies and start scripts), use the JAR file generated in `PROJ_HOME/build/libs` directory.

