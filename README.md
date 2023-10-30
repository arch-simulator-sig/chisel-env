# nanshan Core

Open Source Chip Project by University (OSCPU)

## Dependency

[ESPRESSO](https://github.com/classabbyamp/espresso-logic), branch: master

## Getting Started

First, download all the dependency repositories and set the environment variables as follows.

```bash
export NANSHAN_HOME= ...
export NEMU_HOME= ...
export AM_HOME= ...
export DRAMSIM3_HOME= ...
```

Then, config and make `NEMU`  
```
cd $NEMU_HOME
make riscv64-xs-ref_defconfig
make
```
config `DRAMsim3`, remember read DRAMsim3 README
```
mkdir build
cd build

# cmake out of source build
# if co-simulation
cmake -D COSIM=1 ..

# Build dramsim3 library and executables
make -j8
```

and install `ESPRESSO`.

To run first test:

```
make emu
bash test.sh -t add
```

how to acclerate chisel , refer https://xiangshan-doc.readthedocs.io/zh-cn/latest/tools/compile-and-sim/


### Git Convention

1. Branch for dev: `dev/XxxYyy`. Example: `dev/ScalarPipeline`, `dev/Superscalar`.

1. Only merge stable version (passing all tests) to `develop` branch after being permitted by Li Shi.

1. Never push or merge to `master` branch directly. Make a pull request.

