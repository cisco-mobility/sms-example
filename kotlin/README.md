# SMS Example app - Cisco Mobility Services APIs

This project demonstrates how to authenticate and send an SMS using the Cisco Mobility API Gateway.
This application is written in Kotlin and utilizes the SDK provided by [buf.build](https://buf.build/cisco-mobility/beta/sdks).


# Tabel of Contens
1. [Features](#features)
2. [Getting Started](#getting-started)  
   1. [Prerequisites](#prerequisites)
   2. [Installation](#installation)
   3. [Configuration](#configuration)
   4. [Usage](#usage)

## Features
 * Authenticate
 * Retrieve access token
 * Send SMS

## Getting Started

### Prerequisites
 * Java Development Kit (JDK) 11 or higher

### Installation

Clone the repository
```bash
git clone https://github.com/cisco-mobility/sms-example.git
cd sms-example/kotlin
```

### Configuration
Set up your API credentials
 * Obtain your API client ID and client secret from the [Developer Portal](https://developer.mobility.cisco.com)
 * Export as environment variables
```bash
 $ export CLIENT_ID="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
 $ export CLIENT_SECRET="..."
```

### Usage
Use the supplied bash script to run the application
```bash
$ ./run
```
