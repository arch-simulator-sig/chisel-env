BUILD_DIR = ./build
NANSHAN_HOME = $(shell pwd)

default: verilog

verilog:
	mkdir -p $(BUILD_DIR)
	mill -i nanshan.runMain nanshan.TopMain -td=$(BUILD_DIR)
	find $(BUILD_DIR) -name "*.v" -o -name "*.sv" | xargs cat > $(BUILD_DIR)/SimTop.v

emu: verilog
	cd $(NANSHAN_HOME)/difftest && $(MAKE)  EMU_TRACE=1  emu -j8  
	
init:
	git submodule update --init --recursive
bump:
	git submodule foreach "git fetch origin&&git checkout master&&git reset --hard origin/master"


bsp:
	mill -i mill.bsp.BSP/install

idea:
	mill -i mill.scalalib.GenIdea/idea

help:
	mill -i nanshan.runMain nanshan.TopMain --help

clean:
	-rm -rf $(BUILD_DIR)

.PHONY: clean init bump bsp idea help verilog emu
