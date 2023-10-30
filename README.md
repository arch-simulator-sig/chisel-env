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

Then, config and make `NEMU` and `DRAMsim3`, remember read DRAMsim3 README, and install `ESPRESSO`.

To run first test:

```
make emu
bash test.sh -t add
```


### Git Convention

1. Branch for dev: `dev/XxxYyy`. Example: `dev/ScalarPipeline`, `dev/Superscalar`.

1. Only merge stable version (passing all tests) to `develop` branch after being permitted by Li Shi.

1. Never push or merge to `master` branch directly. Make a pull request.

