package org.firstinspires.ftc.teamcode.next.subsystems

import com.bylazar.configurables.annotations.Configurable
import dev.nextftc.core.subsystems.SubsystemGroup
//import org.firstinspires.ftc.teamcode.helpers.getIndex
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentX
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentY
//import org.firstinspires.ftc.teamcode.next.subsystems.data.Aimbot
import org.firstinspires.ftc.teamcode.next.subsystems.data.Alliance
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Hood
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Turret
import kotlin.math.pow
import kotlin.math.sqrt
@Configurable
object NewOuttake: SubsystemGroup( Turret, Flywheel, Hood) {
    @JvmField var fullManual = false
    @JvmField var autoShoot = false

    @JvmField var goalX = -58.37;
    @JvmField var goalY = 55.64;

    override fun initialize() {

    }

    override fun periodic() {
        if (fullManual) {
            Turret.autoTurret = false
            manualAim()
        } else {
            Turret.autoTurret = true
            auto()
        }

        if(autoShoot) {
            autoShoot()
        }
    }

    // Manual Aim
    fun manualAim() {
        // Jacob's job
    }

    // Auto Functions
    fun auto() {
//        val dist: Double = sqrt((goalX-currentX).pow(2) + (goalY-currentY).pow(2))
//        val values: DoubleArray = Aimbot.getValues(dist)
//
          Hood.updatePosition(0.0)
          Flywheel.updatePid(1000.0)
    }

    fun autoShoot() {
        if(DriveTrain.inShootZone()) {
            // Make commands that acc shoot the ball
        }
    }
}