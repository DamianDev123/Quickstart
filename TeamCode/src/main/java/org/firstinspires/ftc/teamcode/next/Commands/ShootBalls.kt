package org.firstinspires.ftc.teamcode.next.Commands

import com.bylazar.configurables.annotations.Configurable
import com.bylazar.telemetry.PanelsTelemetry
import dev.nextftc.core.commands.Command
import dev.nextftc.hardware.impl.MotorEx
import dev.nextftc.hardware.impl.ServoEx
import org.firstinspires.ftc.teamcode.next.subsystems.Intake
import org.firstinspires.ftc.teamcode.next.subsystems.NewOuttake
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flap
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel
import kotlin.math.abs

@Configurable
class ShootBalls : Command() {

    private var isFlapOpen = false
    private var flapOpenTime = 0L

    private var startTime = System.nanoTime()
    private val tele = PanelsTelemetry.ftcTelemetry

    // --- Shot detection variables ---
    private var lastVelocity = 0.0
    private var dipActive = false
    private var shotCount = 0

    override val isDone: Boolean
        get() {
            // End automatically after 6 seconds (you can change)
            return (System.nanoTime() - startTime) / 1e9 >6.0
        }

    override fun start() {
        Flywheel.flywheelsOn = true

        startTime = System.nanoTime()

        isFlapOpen = false
        flapOpenTime = 0L

        // Reset shot detection
        dipActive = false
        shotCount = 0
        lastVelocity = Flywheel.currentVelocity

        Flap.closeFlap()
    }

    override fun update() {
        tele.update()
        NewOuttake.atSpeed = shotCount;

        val tolerance = 120
        val currentVel = Flywheel.currentVelocity
        val targetVel = Flywheel.targetVelocity
        val atSpeed = abs(currentVel - targetVel) < tolerance

        // ============================================================
        //                SHOT DETECTION USING RPM DIP
        // ============================================================
        val dipThreshold = 200 // RPM drop threshold for detecting a ball feed
        val delta = lastVelocity - currentVel

        // Detect start of dip
        if (delta > dipThreshold && !dipActive) {
            dipActive = true
        }

        // Detect recovery → confirm shot
        if (dipActive && atSpeed) {
            shotCount++
            dipActive = false
            tele.addData("Shots Fired", shotCount)
        }

        lastVelocity = currentVel
        // ============================================================


        // ============================================================
        //                NORMAL SHOOTING CONTROL LOGIC
        // ============================================================

        // --- Open flap once flywheel reaches speed ---
        if (atSpeed && !isFlapOpen) {
            isFlapOpen = true
            Flap.openFlap()
            flapOpenTime = System.nanoTime()
            return
        }

        // Update LED/states for UI
        NewOuttake.atSpeed = if (atSpeed) 1 else 0

        // --- Intake feeding logic ---
        if (isFlapOpen) {
            val elapsedMs = (System.nanoTime() - flapOpenTime) / 1_000_000

            if (elapsedMs > 600) {
                Intake.runIntake()
            } else {
                Intake.stopIntake()
            }
        } else {
            Intake.stopIntake()
        }
    }

    override fun stop(interrupted: Boolean) {
        Flywheel.flywheelsOn = false
        Intake.stopIntake()
        Flap.closeFlap()
    }
}
