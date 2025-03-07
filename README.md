# File Utility Web Application

A web-based application built with Java Spring Boot that provides various file manipulation utilities.

## Features

### 1. Image Compression
- Compress images while maintaining quality
- Adjustable compression quality (0.1 - 1.0)
- Supports common image formats

### 2. PDF Merger
- Merge multiple PDF files into a single document
- Maintains original PDF quality
- Simple drag-and-drop interface

### 3. PDF Password Unlocker
- Remove password protection from PDF files
- Requires correct password input
- Generates unprotected PDF copy

### 4. Image to PDF Converter
- Convert images to PDF format
- Maintains image quality
- Automatic page sizing

## Technologies Used
- Java 8+
- Spring Boot
- Apache PDFBox
- Thumbnailator
- HTML/CSS/JavaScript
- Bootstrap 5

## Setup and Installation
1. Clone the repository
2. Ensure you have Java 8+ and Maven installed
3. Run `mvn clean install` to build the project
4. Run `mvn spring-boot:run` to start the application
5. Access the application at `http://localhost:8081`

## Usage
1. Open the web interface in your browser
2. Select the desired utility
3. Upload your file(s)
4. Configure any necessary settings
5. Process and download the result

## Requirements
- Java 8 or higher
- Maven
- Modern web browser
