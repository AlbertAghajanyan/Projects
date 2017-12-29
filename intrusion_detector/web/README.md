CONTENTS
1. AUTHORS
2. PROJECT DESCRIPTION
3. REQUIREMENTS
4. INSTALLATION
5. PRE-BUILD STEPS
6. RUN
7. FUNCTIONALITY OVERVIEW
8. PROJECT STRUCTURE


1. AUTHORS
-----------------------------
Nelli Melkonyan
E-mail: nelli.melkonyan.im@gmail.com
Khachik Ghazaryan
E-mail: khachik.ghazaryan.im@gmail.com
Vrezh Unanyan
E-mail: vrezh.unanyan.im@gmail.com
Albert Aghajanyan
E-mail: albert.aghajanyan.im@gmail.com

Project Maintainer
>Mane Hambardzumyan
>E-mail: mane_h@instigatemobile.com

Project Contact Person
>Areg Gareginyan
>E-mail: areg@instigatemobile.com

2. PROJECT DESCRIPTION
------------------------------
###### INTRUSION DETECTOR
It is necessary to prepare a system through which private space control will be organized based on video surveillance. The system consists of these two main sub-sections:
>An application that will process the data from video surveillance equipments and will seperate the necessary information.
>An application that will systematically maintain the processed data and make it easy to be used.

3. REQUIREMENTS
------------------------------
* npm v 3.x.x
* Node v 7.x.x
* angular-cli: 1.x.x (optional)

4. INSTALLATION
-----------------------------
4.1 Debian Linux

>$ curl -sL https://deb.nodesource.com/setup_7.x | sudo bash -
>$ sudo apt-get install -y nodejs
>$ npm install npm@3.10.6

4.2 Other requiroments

>$ npm install -g angular-cli
>$ npm install -g typescript
>$ npm install -g typings

5. PRE-BUILD STEPS
------------------------------
$ cd PROJECT_DIR
$ npm install

6. RUN
------------------------------
$ cd $PROJECT_DIR
6.1 Run the project by default
$ npm start
Client URL: http://localhost:4200
Start only client (frontend) on specific port
$ng serve [--port=<port>]
Examples:
* npm start
* ng serve --port 4300

7. FUNCTIONALITY OVERVIEW
-----------------------------
The general page breakdown looks like this:
* Home page (URL /#/home):
    * slider - introduction to the application
* Detected Object page (URL /#/detected-objects):
    * Represents fixed objects 
    * Fixed dates
    * Fixed cameras
* Cameras page (URL /#/cameras):
    * Represents cameras list
* Login page (URL /#/login):
    * Uses JWT (store the token in localStorage)
  

8. PROJECT STRUCTURE
-----------------------------
├── e2e
│   ├── app.e2e-spec.ts
│   ├── app.po.ts
│   └── tsconfig.e2e.json
├── karma.conf.js
├── package.json
├── protractor.conf.js
├── README.md
├── src
│   ├── app
│   │   ├── app.component.css
│   │   ├── app.component.html
│   │   ├── app.component.spec.ts
│   │   ├── app.component.ts
│   │   ├── app.module.ts
│   │   ├── cameras
│   │   │   ├── cameras.component.css
│   │   │   ├── cameras.component.html
│   │   │   ├── cameras.component.spec.ts
│   │   │   └── cameras.component.ts
│   │   ├── detected-objects
│   │   │   ├── detected-objects.component.css
│   │   │   ├── detected-objects.component.html
│   │   │   ├── detected-objects.component.spec.ts
│   │   │   └── detected-objects.component.ts
│   │   ├── home
│   │   │   ├── home.component.css
│   │   │   ├── home.component.html
│   │   │   ├── home.component.spec.ts
│   │   │   └── home.component.ts
│   │   ├── login
│   │   │   ├── login.component.css
│   │   │   ├── login.component.html
│   │   │   ├── login.component.spec.ts
│   │   │   └── login.component.ts
│   │   ├── navigation-bar
│   │   │   ├── navigation-bar.component.css
│   │   │   ├── navigation-bar.component.html
│   │   │   ├── navigation-bar.component.spec.ts
│   │   │   └── navigation-bar.component.ts
│   │   └── routes.ts
│   ├── assets
│   │   └── images
│   │       ├── cam.png
│   │       └── icon.png
│   ├── environments
│   │   ├── environment.prod.ts
│   │   └── environment.ts
│   ├── index.html
│   ├── main.ts
│   ├── polyfills.ts
│   ├── styles.css
│   ├── test.ts
│   ├── tsconfig.app.json
│   ├── tsconfig.spec.json
│   └── typings.d.ts
├── tsconfig.json
└── tslint.json


