import chisel3._


class ImmGenIO(val width:Int) extends Bundle {
    val inst    = Input(UInt(width.W))
    val sel     = Input(Bool())
    val out     = Output(UInt(width.W))
}


trait ImmGen extends Module {
    val width : Int
    val test : UInt
    val io : ImmGenIO
}


class ImmGemWire(val width:Int) extends ImmGen {
    val io = IO(new ImmGenIO(width))
    val test2 = test.asSInt
}