# pmMDA — poor man MDA

"MDA" (Model Driven Architecture) code generation engine based on Apache Velocity,
implemented in Java. Models are defined with UML tools (e.g. ArgoUML, Rational Rose) and
expanded through generators and templates into application code for 2-tier, CORBA and J2EE
platforms.

* Original project: <https://sourceforge.net/projects/pmmda/> (SourceForge, created 2004-05-25,
  development stopped 2009).
* Import: the original CVS repository was obtained from the SourceForge CVS snapshot
  <https://sourceforge.net/code-snapshots/cvs/p/pm/pmmda.zip> (SourceForge's CVS service is
  read-only / decommissioned) and converted to Git with `git cvsimport`. Commit history, CVS
  author names and the release tags of the CVS repository are preserved.
* Last upstream commit: 2009-01-25 (Adrian R. Bachofen).

## License

Apache License 2.0 — see [`LICENSE`](LICENSE). The copy shipped in the original tree is
[`src/ApacheLicense-2_0.txt`](src/ApacheLicense-2_0.txt); most source files carry the
corresponding Apache-2.0 header, and the SourceForge project lists the license as
"Apache License V2.0". Copyright remains with the original authors (see the commit history);
third-party libraries under `lib/` remain under their own licenses.
