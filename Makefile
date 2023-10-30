BUILD_DIR = ./build
NANSHAN_HOME = $(shell pwd)

default: verilog

verilog:
	mkdir -p $(BUILD_DIR)
	mill -i nanshan.runMain nanshan.TopMain  -td $(BUILD_DIR)

emu: verilog
	cd $(NANSHAN_HOME)/difftest && $(MAKE)  EMU_TRACE=1  emu -j8  
	
# WITH_DRAMSIM3=1

# emu-direct:
# 	cd $(NANSHAN_HOME)/difftest && $(MAKE) WITH_DRAMSIM3=1 EMU_TRACE=1 emu -j

# soc: sim-verilog
# 	/bin/bash ./test.sh -s

bsp:
	mill -i mill.bsp.BSP/install

idea:
	mill -i mill.scalalib.GenIdea/idea

help:
	mill -i nanshan.runMain nanshan.TopMain --help

clean:
	-rm -rf $(BUILD_DIR)

.PHONY: clean bsp idea help verilog emu
