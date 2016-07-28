# graalJSchakraTestDriver
This project is about creating a test-driver which evaluates the [oracle graal.js](http://www.oracle.com/technetwork/oracle-labs/program-languages/overview/index.html) implementation against the [ms chakra core tests](https://github.com/Microsoft/ChakraCore).
Will include a human readable / formatted html result output.
Right now it looks like the test-driver will be available to execute arbitrary test if you exchange the 

## status
- [x] checkout of (just) the chakra tests
- [x] mockup of the [test-result HTML](https://rawgit.com/dmnk/graalJSchakraTestDriver/master/data/resultExample.html)
  - features
    - responsive layout (bootstrap / jquery)
    - on-demand loading of the tests / results
    - syntax & line-number highlightning for the test and diff files [prism.js](http://prismjs.com/download.html)
- [x] use github ;-)
- [x] fetching the tests in provided base-directory
- [x] in/excluding tests based on white/blacklists
- [x] excluding tests known to crash the VM based on a additional "crashlist"
- [x] executing the tests
- [ ] determine the fail reason / line
- [x] exporting the results
  - different formats (html & fail/pass filename only)
  - text should be ready
 - [x] configuration
  	- through cli
  	- defaults out of a class
  	- stackable, so more configuration providers can be added 
- [x] JUnit tests for
	- TestFetcher
	- HTMLResultExporter
	- TestInitiator
	- CLIConfigProvider
  
## get the open-sourced JS tests from ms chakra core project
```
mkdir chakraTests
cd chakraTests
git init
git remote add -f origin https://github.com/Microsoft/ChakraCore.git
git config core.sparseCheckout true
echo "test" >> .git/info/sparse-checkout
git pull origin master
```
