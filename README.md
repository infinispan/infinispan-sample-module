# Sample Infinispan Module
The purpose of this sample module is to demonstrate the changes made to Infinispan in [ISPN-256](https://issues.jboss.org/browse/ISPN-256?focusedCommentId=12586154#comment-12586154) to allow modules - such as `query` - to implement their own commands outside of the `core` module.

## Status
This sample is a fully working sample and the sample usage class in `src/test/java` can be examined and even run to demonstrate custom commands working.  TRACE level logging is recommended to examine what goes on.

## Versions
For this to work, you currently need to build and install Infinispan 5.0.0-SNAPSHOT from [this topic branch](https://github.com/maniksurtani/infinispan/commits/t_pluggable_commands), until it is merged into upstream.

# Instructions for module authors
Here's what you need to do:

 * Create a `infinispan-module.properties` file (see sample)
 * Declare implementation names of the `ModuleCommandFactory` and `ModuleCommandInitializer` implementations you've created for your module in the properties file
 * Actually implement them to be able to construct your new commands
 * Implement your commands and the rest of your module

Please provide feedback on this on [ISPN-256](https://issues.jboss.org/browse/ISPN-256?focusedCommentId=12586154#comment-12586154).

