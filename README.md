## Skinny application example

[![Build Status](https://travis-ci.org/skinny-framework/skinny-framework-example.svg?branch=master)](https://travis-ci.org/skinny-framework/skinny-framework-example)
[![Coverage Status](https://coveralls.io/repos/skinny-framework/skinny-framework-example/badge.png?branch=master)](https://coveralls.io/r/skinny-framework/skinny-framework-example?branch=master)

### Just run

Run Skinny app now!

    ./skinny db:migrate
    ./skinny run

### Examples

I can see how to write controller code here:

[src/main/scala/controller/CompaniesController.scala](https://github.com/skinny-framework/skinny-framework-example/blob/master/src/main/scala/controller/CompaniesController.scala)

Skinny ORM usage:

[src/main/scala/model/Member.scala](https://github.com/skinny-framework/skinny-framework-example/blob/master/src/main/scala/model/Member.scala)

Simple CRUD views:

[src/main/webapp/WEB-INF/views/companies](https://github.com/skinny-framework/skinny-framework-example/tree/master/src/main/webapp/WEB-INF/views/companies)

### Run tests

    ./skinny db:migrate test
    ./skinny test
    ./skinny testOnly integrationtest.*Spec

### How to use coveralls.io

- enable your GitHub repo on https://coveralls.io/
- `gem instanll travis`
- `travis encrypt COVERALLS_REPO_TOKEN={your token} -add -r {github user}/{github repo}`

### Skinny Framework

http://skinny-framework.org/

