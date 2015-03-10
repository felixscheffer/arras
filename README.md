Arras is a set of open-source components for [Apache Tapestry 5.4](http://tapestry.apache.org).

[![Build Status](https://secure.travis-ci.org/fscheffer/arras.png)](http://travis-ci.org/fscheffer/arras)
[![Sauce Test Status](https://saucelabs.com/browser-matrix/fscheffer.svg)](https://saucelabs.com/u/fscheffer)

## Download
Releases are available on [Maven Central](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.fscheffer%22).

```
<dependency>
	<groupId>com.github.fscheffer</groupId>
	<artifactId>arras-components</artifactId>
	<version>1.1.1</version>
</dependency>
```

```
<dependency>
	<groupId>com.github.fscheffer</groupId>
	<artifactId>arras-cms</artifactId>
	<version>1.1.1</version>
</dependency>
```
Snapshots are available on https://oss.sonatype.org/content/groups/public/

## Arras Components

* TabGroup
* Dropdown
* Lightbox and LightboxBody (requires jQuery - based on [Colorbox](http://www.jacklmoore.com/colorbox/))
* DataTable (requires jQuery - based on [DataTables](http://www.datatables.net/))
* Icon (based on [font-awesome](http://fortawesome.github.io/Font-Awesome/))
* RemoteSubmit (triggers form submission from the outside)
* MediumEditor (based on [Medium-Editor](https://github.com/daviferreira/medium-editor))
* Player (audio and video - based on [video.js](http://www.videojs.com/))

Check out the demo:
https://arras-components.herokuapp.com/


## Arras CMS

Arras CMS provides simple content management functionality. It allows you to change text and images without having to redeploy your website.

![alt tag](https://github.com/fscheffer/arras/blob/master/arras-cms.png)

Check out the demo (resets every 24h):
http://arras-cms.herokuapp.com/

To log in, just click on "sign in". You don't have to enter a username or a password. 

The war file is also available on Maven Central.

## Arras Bootstrap

A convenience module to add Bootstrap's less files to your project. 

Override Tapestry's default bootstrap.css with your own less file (e.g. your-project.less)

```
    @Contribute(JavaScriptStack.class)
    @Core
    public static void overrideBootstrapCSS(OrderedConfiguration<StackExtension> conf) {
        conf.override("bootstrap.css", StackExtension.stylesheet("META-INF/assets/your-project.less"));
    }
```

Import Bootstrap's less files in your project's less file and adjust Bootstrap variables as you wish:

```
@import "arras/bootstrap/less/bootstrap.less";

// adjust bootstrap here, e.g:
@brand-primary:         purple;
```


## Name
Arras is [a city in France](http://en.wikipedia.org/wiki/Arras) which was specialized in fine wool tapestries in the 14th and 15th centuries. The term Arras is still used as a synonym for a rich tapestry. 

## Release History
* 1.2.0 (unreleased)
 - added "dom" and "placeholder" parameters to DataTable
 - added LoadingOverlay mixin
 - added Select2 and MultiSelect2 components (based on select2 js)
 - added DataSourceLoop component
 - added arras-bootstrap module
 - added arras-mail module
 
* 1.1.2 (2014-11-27)
 - improved reliability of selenium tests
 - switched to Java 6 (instead of Java 7)
 - support for fontawesome 3.x icon css classes
 
* 1.1.1 (2014-11-14)
 - fixed Writing to a @Content property did not work when the contentId was not specified
 
* 1.1.0 (2014-11-10)
 - added audio and video player (see Player component)
  
* 1.0.0 (2014-11-07)
 - first release
 
## Roadmap
- replace medium-editor with medium.js
- add a slider component (based on css3 effects rather than javascript)
- add a ajax upload component (which is not based on jquery)

## License
Arras is distributed under [The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt).

Arras includes files from other open-source projects which are distributed under the [MIT license](http://opensource.org/licenses/mit-license.php).