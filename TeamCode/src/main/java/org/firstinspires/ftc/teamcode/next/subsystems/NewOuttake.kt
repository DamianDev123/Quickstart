package org.firstinspires.ftc.teamcode.next.subsystems

import com.bylazar.configurables.annotations.Configurable
import com.pedropathing.follower.Follower
import com.pedropathing.geometry.Pose
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
    val redGoalTag = Limelight.toPedro(Pose(
        -1.48267,
        1.4133,
        0.0  // (AprilTags don't require heading for aiming)
    ))
    @JvmField var goalX = redGoalTag.x;
    @JvmField var goalY = redGoalTag.y;
    var ddd = 0.0;
    var atSpeed = 0;
    var pose : Pose = Pose(0.0,0.0,0.0);

    var dist: Double = 0.0;

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
         dist = sqrt((goalX-currentX).pow(2) + (goalY-currentY).pow(2))
//        val values: DoubleArray = Aimbot.getValues(dist)
//
          Hood.updatePosition(0.0)
          Flywheel.updatePid(1700.0)
    }

    fun autoShoot() {
        if(DriveTrain.inShootZone()) {
            // Make commands that acc shoot the ball
        }
    }
}