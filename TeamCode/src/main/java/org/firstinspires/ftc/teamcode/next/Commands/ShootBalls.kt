package org.firstinspires.ftc.teamcode.next.Commands

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import dev.nextftc.core.commands.Command
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import org.firstinspires.ftc.teamcode.next.subsystems.Intake
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flap
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel

@Configurable
class ShootBalls : Command() {



    private var isFlapOpen = false
    private var flapOpenTime = 0L

    private var startTime = System.nanoTime()
    private var dipCount = 0                 // <-- count dips
    private var dipActive = false            // avoid double-counting during one dip
    val tele = PanelsTelemetry.ftcTelemetry;
    override val isDone: Boolean
        get() {
            // end early after 2 dips
            if (dipCount >= 2) return true
            return (System.nanoTime() - startTime) / 1e9 > 6.0
        }

    override fun start() {
        Flywheel.flywheelsOn = true
        startTime = System.nanoTime()
        isFlapOpen = false
        flapOpenTime = 0L
        dipCount = 0
        dipActive = false
        Flap.closeFlap();
    }

    override fun update() {
        tele.addData("dips",dipCount);
        tele.update();
        val tolerance = 50
        val dipThreshold = 300

        val speedError = Flywheel.currentVelocity - Flywheel.targetVelocity
        val atSpeed = kotlin.math.abs(speedError) < tolerance

        // 🚨 --- Velocity dip detection ---
        if (speedError < -dipThreshold && isFlapOpen) {
            if (!dipActive) {
                dipCount++          // <-- count dip ONCE
                dipActive = true
            }

            // pause feeding + close
            isFlapOpen = false
            Intake.runIntake();
            //Flap.closeFlap();
            return
        } else {
            // dip has ended → allow next dip to be counted
            dipActive = false
        }

        // --- Open flap once at speed ---
        if (atSpeed && !isFlapOpen) {
            isFlapOpen = true
            Flap.openFlap()
            flapOpenTime = System.nanoTime()
            return
        }
        // --- Delay before feeding ---
        if (isFlapOpen) {
            val elapsedMs = (System.nanoTime() - flapOpenTime) / 1_000_000

            if (elapsedMs > 600) {
                Intake.runIntake();
            } else {
                Intake.stopIntake();
            }
        } else {
            Intake.stopIntake();
        }
    }

    override fun stop(interrupted: Boolean) {
        Flywheel.flywheelsOn = false
        Intake.stopIntake();
        //Flap.closeFlap();
    }
}
