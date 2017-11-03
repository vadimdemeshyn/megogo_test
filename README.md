# megogo_test


                                                        Project Structure
                                                        
                                             src
                                             |
                               ______________test
                              |              |
                              |              java(all source code of project)
                              |              |                           
                              |            
  |                           |________________    |                                 |                          |                         |                             
  ChannelData                                  |   Parser                            Program                    Verificator               Tests
                                               |
  this class is made to initialize Maps        |   the main purpose of this class    Here we create Program     This class contains       This class contains tests which we 
  which contain mappings for channels          |   is to send requests to endpoint,  objects from XML and       all fields and            run using TestNG framework
  specified in src/test/resources/xmlProgramms |   save XML file programms and also  JSON response for future   methods for
  directory                                    |   fill Arraylists with up-to date   comparison                 verifying JSON
                                               |   programs for future comparison                               schemas
                                               |              
                                               | 
                                               |                                 
                                             resources
                                                 |
  |                                                |                                           |
  csvMappingFiles                                schemas                                   xmlProgramms
this directory contains CSV                     this directory contains JSON               in this directory we store
files which contain info                        schemas we use to validate                 all saved XML files
in format                                       JSONs from responses
channelName,channelXMLId,channelRequestID

How to run project

mvn -Dchannel="СТБ" clean test
-D options takes parameters for checking any channel we want (task declared 
that we need to have an option to validate other channels too).
