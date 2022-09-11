# Chasmix
### Sponge Mixin rewritten as an experimental Chasm frontend

#### [Design Statement ->](https://gist.github.com/FoundationGames/d8d14f01a62ab6f66941c7fcb2becc33)

#### Summary:
- Chasm is the primary (and eventually, only) bytecode library to be loaded on the Quilt mod loader
- In order to support Fabric mods (and provide an excellent, high level ASM interface to Quilt modders), Chasmix intends 
  to serve as a reimplementation and near-drop-in-replacement for Mixin on the Quilt modding toolchain
  
#### Currently Implemented:
- Parsing Mixin config files (mixins.json):
  - `package`: Mixin package
  - `mixins`, `client`, `server`: Environment-based Mixin class lists
- Processing Mixin classes defined in Mixin config
- Gradle plugin to convert mixins into Chasm on project build
- **Implemented Mixin Features:**
  - Adding and implementing Interfaces
  - Shadowing Fields and Methods
    - NOTE: Annotations on shadowed members are not yet added to the original
  - Adding Fields and Methods
  - Adding to the class initializer
  - Using Pseudo to target a class that may not be present

#### Get Started:
*NOTE: Chasmix is experimental and still a work-in-progress.* <br/>
- Add Chasmix's Gradle plugin: `id 'foundationgames.chasmix.gradle'` (NOTE: currently not published)
- Add Chasmix as a dependency: `implementation 'foundationgames:chasmix:<ver>'`
- Create your Mixin files, and add them to your Mixin config file
- Subsequent builds should contain Chasm transformers (NOTE: Chasmix is not yet feature-complete, transformers will likely not work)

For use as a library (in order to process Mixin into Chasm outside of development environments), see documentation of classes in `foundationgames.chasmix.api`