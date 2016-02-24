# graalJSchakraTestDriver
This project is about creating a test-driver which evaluates the [oracle graal.js](http://www.oracle.com/technetwork/oracle-labs/program-languages/overview/index.html) implementation against the [ms chakra core tests](https://github.com/Microsoft/ChakraCore).
Will include a human readable / formatted html result output.
Right now it looks like the test-driver will be available to execute arbitrary test if you exchange the 

## status
**the checked items of this task list aren't displayed as checked in Firefox 44**
- [x] checkout of (just) the chakra tests
- [x] mockup of the [test-result HTML](https://rawgit.com/dmnk/graalJSchakraTestDriver/master/data/resultExample.html)
  - features
    - responsive layout (bootstrap / jquery)
    - on-demand loading of the tests / results
    - syntax & line-number highlightning for the test and diff files [prism.js](http://prismjs.com/download.html)
- [x] use github ;-)
- [ ] fetching the tests in provided base-directory
- [ ] in/excluding tests based on white/blacklists
- [ ] excluding tests known to crash the VM based on a additional "crashlist"
- [ ] executing the tests
- [ ] exporting the results
  - different formats (html & fail/pass filename only)
  
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
