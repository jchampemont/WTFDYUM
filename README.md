# WTFDYUM 
## a.k.a. Why The Fuck Did you Unfollow me ?
[![Build Status](https://travis-ci.org/jchampemont/WTFDYUM.svg?branch=master)](https://travis-ci.org/jchampemont/WTFDYUM)
[![Coverage Status](https://coveralls.io/repos/jchampemont/WTFDYUM/badge.svg?branch=master&service=github)](https://coveralls.io/github/jchampemont/WTFDYUM?branch=master)

A small tool to find out who stopped following you on Twitter.

### For developers
#### 3 minutes installation :

    - Clone, fork or download the source code from this Github page
    - Create a twitter application here : https://apps.twitter.com/
    - Install Maven 3
    - Edit file [src/main/filters/dev.properties](https://github.com/jchampemont/WTFDYUM/blob/master/src/main/filters/dev.properties) with your twitter app credentials
    - Start a redis development instance with `mvn redis:run`
    - Start the application with `mvn spring-boot:run`
    - Connect to the application at http://127.0.0.1:8080
    
### Installation for production use

Download latest release from here : https://github.com/jchampemont/WTFDYUM/releases

See bundled INSTALL.md file for installation details.

### Contributing
I am happy to accept any pull request as long as it respects the following guidelines :

    - Meets some basic code quality requirements
    - Please do some basic unit testing on your code
    - Use current technology stack before introducing any new dependency
    - You accept your code to be licensed under the Apache License, Version 2.0
    - NoteDown should stay *KISS* : Keep It Simple, Stupid.

Feel free to add your name on the list of contributors below.

### Contributors

- Jean Champémont

### License

Copyright 2015 [Jean Champémont](http://www.jeanchampemont.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this application except in compliance with the License.
You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
