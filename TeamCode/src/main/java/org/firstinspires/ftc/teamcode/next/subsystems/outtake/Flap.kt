package org.firstinspires.ftc.teamcode.next.subsystems.outtake

import com.bylazar.configurables.annotations.Configurable
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel
import java.io.Flushable

@Configurable
object Flap: Subsystem {
    private val outakeFlap = ServoEx("letIn")
    @JvmField var flapClosed = 0.9;
    @JvmField var flapOpened = 0.0;
    @JvmField var flapPos = 0.0;
    override fun periodic() {
        outakeFlap.position = flapPos
    }
    val closeFlap = InstantCommand {
        flapPos = flapOpened
    }

    val openFlap = InstantCommand {
        flapPos = flapOpened
    }

    fun closeFlap(){
       //outakeFlap.position = flapClosed;
    }
    fun openFlap(){
        outakeFlap.position = flapOpened;
    }

}