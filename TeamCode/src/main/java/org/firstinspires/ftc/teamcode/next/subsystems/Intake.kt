package org.firstinspires.ftc.teamcode.next.subsystems

import com.bylazar.configurables.annotations.Configurable
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel
import java.io.Flushable

@Configurable
object Intake: Subsystem {

    private val transferMotor = MotorEx("Transfer")
    private val intakeMotor = MotorEx("Intake")
    val  runIntake = InstantCommand {
        transferMotor.power = -1.0
        intakeMotor.power = -1.0;
    }

    val stopIntake = InstantCommand {
        transferMotor.power = 0.0
        intakeMotor.power = 0.0
    }

}