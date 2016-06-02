# Product charts and Jhipster

Web demonstration of product chart. 
See also [https://github.com/FAMILIAR-project/productcharts](product charts project) and [http://opencompare.org](opencompare.org)

A few notes: 
 * ad-hoc adding of JS Plot.ly (I directly edit index.html and put it in assets folder; obviously relying on bower facilities is a better practice)
 * adding a custom view boils down to add a new html file and then edit an AngularJS controller (eg see pchart.js and productchart.html)
 * adding a dependency with Maven is quite easy (especially the dependency is already a Maven project) 
 * ad-hoc adding of a resource / REST mapping (e.g., PchartsResource)  



This application was generated using JHipster (see [https://jhipster.github.io](https://jhipster.github.io)).


