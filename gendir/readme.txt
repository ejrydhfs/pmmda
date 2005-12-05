The directory "gendir" contains subdirectories where the various cartridges 
generates source code and configuration artifacts.

Only artifacts which are not extended by the users should be generated in 
these directories. The list of empty directories defined in the directory
are.

- corba-client
  contains all generated artifacts for the client CORBA side
- corba-common
  contains all generated artifacts common to client and server CORBA side
- corba-idl
  contains all generated artifacts defining the IDL interfaces
- corba-server
  contains all generated artifacts for the server CORBA side
- j2ee-client
  contains all generated artifacts for the client J2EE side
- j2ee-common
  contains all generated artifacts common to client and server J2EE side
- j2ee-server
  contains all generated artifacts for the server J2EE side
- orm-server
  contains all generated artifacts for the ORM mapper to the relational 
  database
- rich-client
  contains all generated artifacts for the client in a two tiers system
