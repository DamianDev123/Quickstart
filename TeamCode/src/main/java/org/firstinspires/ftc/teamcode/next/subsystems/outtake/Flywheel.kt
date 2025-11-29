package org.firstinspires.ftc.teamcode.next.subsystems.outtake

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.control.feedforward.BasicFeedforwardParameters
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.hardware.impl.Direction
import dev.nextftc.hardware.impl.MotorEx
import java.time.Instant
@Configurable
object Flywheel: Subsystem {
    private val f1 = MotorEx("Shooter")
    val tele = PanelsTelemetry.ftcTelemetry;
    @JvmField var flywheelPID = PIDCoefficients(0.01, 0.0, 0.0)
    @JvmField var flywheelFF = BasicFeedforwardParameters(0.00042, 0.0, 0.19)
     var flywheelController = controlSystem {
        velPid(flywheelPID)
        basicFF(flywheelFF)
    }

    @JvmField var targetVelocity = 0.0
    @JvmField var currentVelocity = 0.0

    @JvmField var flywheelsOn = false

    var motorRpm: Double = 0.0
    override fun initialize() {
        f1.motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        f1.motor.direction = DcMotorSimple.Direction.REVERSE
    }
    override fun periodic() {
        motorRpm = f1.velocity * 60.0/28.0

        f1.power = flywheelController.calculate(f1.state)
        if (flywheelsOn) {
            flywheelController.goal = KineticState(0.0, targetVelocity)
        }
        else {
            flywheelController.goal = KineticState(0.0, 0.0)
        }
        currentVelocity = f1.velocity;

        tele.run {
            addData("targetVelo", targetVelocity)
            addData("Vel", f1.motor.velocity)
            addData("RPM target", targetVelocity*60.0/28.0)
            addData("flywheel goal", flywheelController.goal)
        }
    }

    fun updatePid(velocity:Double) {
        targetVelocity = velocity
    }

    val spin = InstantCommand {
        flywheelsOn = true
    }

    val stop = InstantCommand {
        flywheelsOn = false
    }

    val backOut = InstantCommand {
        stop.schedule()
        f1.power = -0.5
    }

    val stopPower = InstantCommand {
        f1.power = 0.0
    }
}