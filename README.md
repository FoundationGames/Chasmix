# Chasmix
### Sponge Mixin rewritten as an experimental Chasm frontend

#### [Design Statement ->](https://gist.github.com/FoundationGames/d8d14f01a62ab6f66941c7fcb2becc33)

#### Summary:
- Chasm is the primary (and eventually, only) bytecode library to be loaded on the Quilt mod loader
- In order to support Fabric mods (and provide an excellent, high level ASM interface to Quilt modders), Chasmix intends 
  to serve as a reimplementation and near-drop-in-replacement for Mixin on the Quilt modding toolchain
  
#### Currently Implemented:
- Loading Mixin classes
- Parsing Mixin config files (mixins.json):
  - `package`: Mixin package
  - `mixins`, `client`, `server`: Environment-based Mixin class lists
- Interface Merging

#### Get Started:
*NOTE: Chasmix is experimental and still a work-in-progress.* <br/>
Currently, Chasmix does not have any functionality in mod development environments. <br/>
For use as a library, see documentation of classes in `foundationgames.chasmix.api`