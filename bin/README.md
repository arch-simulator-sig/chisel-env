# 一生一芯基础任务测试用例

本目录存放`一生一芯`[基础任务](https://oscpu.github.io/ysyx/wiki/tasks/basic.html)需要使用的测试用例。目录结构如下：

```shell
.
├── custom-output					# 0x7b自定义打印指令测试用例
│   ├── benchmark
│   ├── hello
│   ├── interrupt-test
│   ├── mario
│   ├── rt-thread
│   ├── time-test
│   └── yield-test
├── inst.bin						# 包含3条addi指令，可用于测试cpu例程
├── inst_diff.bin					# 包含3条addi指令和1条0x6b，可用于测试cpu_diff和chisel_cpu_diff例程
├── non-output						# 无输出测试用例
│   ├── coremark
│   ├── cpu-tests
│   ├── dhrystone
│   ├── microbench
│   └── riscv-tests
└── README.md
```

# 测试用例说明

所有测试用例使用`0x6b`指令作为结束指令，当执行到`0x6b`指令时，将`a0`寄存器的值作为`trap code`提交给difftest即可。`trap code`为0表示正确执行并退出程序，非0表示断言失败。

## non-output

该目录下的测试用例运行时无输出，可用于测试CPU指令实现的正确性。

## custom-output

该目录下的测试用例使用`0x7b`指令作为输出指令，，可用于测试CPU指令实现的正确性。CPU在执行该指令时，将`a0`寄存器的值作为[ASCII](https://en.wikipedia.org/wiki/ASCII)字符打印出来。其中`interrupt-test`和`rt-thread`需要实现`CLINT`，关于`CLINT`的地址，可以参考[这里](https://chromitem-soc.readthedocs.io/en/latest/clint.html)。