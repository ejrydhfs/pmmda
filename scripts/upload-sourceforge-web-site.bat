REM upload the pmMDA web site on sourceforge using putty and sourceforge 
REM mechanisms through a secure SSH connection
c:\bin\putty\pscp -r -C d:\data\projects\pmmda\build\site\* marcelbaumann@shell.sourceforge.net:/home/groups/p/pm/pmmda/htdocs
 