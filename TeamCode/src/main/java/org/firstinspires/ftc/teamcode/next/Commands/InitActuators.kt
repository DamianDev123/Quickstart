package org.firstinspires.ftc.teamcode.next.Commands

import com.bylazar.configurables.annotations.Configurable
import dev.nextftc.core.commands.Command
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import org.firstinspires.ftc.teamcode.next.subsystems.Intake
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flap
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Hood
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.Subsystem

@Configurable
object InitActuators: Subsystem {


   val init = SequentialGroup(
        Hood.closeHood,
        Intake.stopIntake,
        Flap.closeFlap,
        InstantCommand { Flywheel.flywheelsOn = false }
    )
    fun init(){
        Hood.closeHood()
        Intake.stopIntake()
        Flap.closeFlap()
        Flywheel.flywheelsOn = false;
    }
}
