# Sample Infinispan Module
The purpose of this sample module is to demonstrate the changes made to Infinispan in [ISPN-256](https://issues.jboss.org/browse/ISPN-256?focusedCommentId=12586154#comment-12586154) to allow modules - such as `query` - to implement their own commands outside of the `core` module.

## Status
This sample is a fully working sample and the sample usage class in `src/test/java` can be examined and even run to demonstrate custom commands working.  TRACE level logging is recommended to examine what goes on.

## Versions
For this to work, you currently need Infinispan 5.1.0.CR2 or higher.

# Instructions for module authors
Here's what you need to do:

 * Create a `org.infinispan.commands.module.ModuleCommandExtensions` file in META-INF/services folder (see sample).
 * Declare implementation name of `ModuleCommandExtensions` implementation you've created for your module. Remember that this indirectly requires you to implement both `ExtendedModuleCommandFactory` and `ModuleCommandInitializer`.
 * Actually implement them to be able to construct your new commands.
 * Implement your commands and the rest of your module.
 * Add `<module.skipComponentMetaDataProcessing>false</module.skipComponentMetaDataProcessing>` property to your maven build pom file.
 * Run `mvn -Dmaven.test.skip.exec=true install` so that metadata index file is generated.
 * Implement `MetadataFileFinder` and provide the metadata file name that has been generated.
 * Create a `org.infinispan.factories.components.ModuleMetadataFileFinder` file in META-INF/services folder which the name of the class that implements `MetadataFileFinder`.

Any feedback please direct it to the [Infinispan forums](http://community.jboss.org/en/infinispan?view=discussions).

