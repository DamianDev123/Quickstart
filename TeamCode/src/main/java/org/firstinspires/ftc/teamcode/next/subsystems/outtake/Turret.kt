package org.firstinspires.ftc.teamcode.next.subsystems.outtake

import com.qualcomm.robotcore.hardware.DcMotor
import dev.nextftc.control.KineticState
import dev.nextftc.control.builder.controlSystem
import dev.nextftc.control.feedback.PIDCoefficients
import dev.nextftc.core.commands.utility.InstantCommand
import dev.nextftc.core.subsystems.Subsystem
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.hardware.impl.MotorEx
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentHeading
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentX
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentY
import org.firstinspires.ftc.teamcode.next.subsystems.NewOuttake.goalX
import org.firstinspires.ftc.teamcode.next.subsystems.NewOuttake.goalY
import org.opencv.core.Mat
import kotlin.math.PI
import kotlin.math.atan2

object Turret : Subsystem {

    private val turret = MotorEx("Turret")
    private val gearRatio = 112.0 / 22.0 // must be double

    private val ppr = 537.7                           // motor ticks per revolution
    private val ticksPerDeg = (ppr * gearRatio) / 360
    var imAming: Int = 0;
    @JvmField var autoTurret = false
    @JvmField var turretPID = PIDCoefficients(0.002, 0.0, 0.0)
    var turretController = controlSystem {
        posPid(turretPID)
    }

    override fun periodic() {
        if (autoTurret) {
            autoAim()
        }
        turret.power = turretController.calculate(KineticState((turret.motor.currentPosition*1.0), 0.0))
    }

    override fun initialize() {
        turret.motor.mode = DcMotor.RunMode.RUN_USING_ENCODER;
    }

    fun zero() {
        turret.motor.mode = DcMotor.RunMode.RUN_USING_ENCODER;
        turret.motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    private fun autoAim() {
//        val mu = atan2(goalY - currentY, goalX - currentX)
//        val deltaHeading = normalizeAngle(mu - currentHeading)
//
//        imAming = degToTicks(Math.toDegrees(deltaHeading));
//
//        val yawDeg = degToTicks(Math.toDegrees(deltaHeading))*1.0;
//        val sanitizedYaw = yawDeg.coerceIn(-180.0, 180.0)
//        val inTicks = degToTicks(sanitizedYaw)
//
//        turretController.goal = KineticState(inTicks*1.0, 0.0)
        turret.power = turretController.calculate(KineticState((turret.motor.currentPosition*1.0), 0.0))


    }
    fun goToYaw(yawDeg: Double): InstantCommand { // input in degrees

        return InstantCommand {
            turret.motor.mode = DcMotor.RunMode.RUN_USING_ENCODER;
            val sanitizedYaw = yawDeg.coerceIn(-180.0, 180.0)
            val inTicks = degToTicks(sanitizedYaw)

            turretController.goal = KineticState(inTicks*1.0, 0.0)
            turret.power = turretController.calculate(KineticState((turret.motor.currentPosition*1.0), 0.0))
        }

    }


    fun getYaw(): Double {
        return turret.currentPosition
    }

    private fun degToTicks(deg: Double): Int {
        return (deg * ticksPerDeg).toInt()
    }

    fun normalizeAngle(angleRadians: Double): Double {
        var angle = angleRadians % (2.0 * PI)
        if (angle <= -PI) angle += 2.0 * PI
        if (angle >  PI) angle -= 2.0 * PI
        return angle
    }
}
