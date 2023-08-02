import  chisel3._
import  nanshan._


//implement simple bpu and pre-decode with RAS

class Fetch extends Module {
    val io = IO(new Bundle{
        val pc = Output(UInt(32.W))
        val inst = Output(UInt(32.W))
        val ram = Flipped(new ROMIO)
    })


    val pc = RegInit(resetVector.U(32.W))

    io.ram.addr := io.pc
    io.ram.en := true.B

    io.inst := io.ram.data
}