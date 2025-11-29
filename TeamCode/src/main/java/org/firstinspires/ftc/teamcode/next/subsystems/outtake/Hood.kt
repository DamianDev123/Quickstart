package org.firstinspires.ftc.teamcode.next.subsystems.outtake

import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.ServoEx
import com.bylazar.configurables.annotations.Configurable
import dev.nextftc.core.commands.utility.InstantCommand
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flap.flapOpened

@Configurable
object Hood: Subsystem {
    private val hoodServo = ServoEx("flap")
    @JvmField var In = 0.0;
    @JvmField var Out = 0.0;
    @JvmField var hoodPosition = 0.0

    override fun periodic() {
        hoodServo.position = hoodPosition
    }
    fun lerp(a: Double, b: Double, t: Double): Double {
        return a + (b - a) * t
    }
    fun updatePosition(position: Double) {
        hoodPosition = lerp(In, Out,position);
    }
    val closeHood = InstantCommand {
        updatePosition(In)
    }
    fun closeHood(){
        updatePosition(In)
    }
}