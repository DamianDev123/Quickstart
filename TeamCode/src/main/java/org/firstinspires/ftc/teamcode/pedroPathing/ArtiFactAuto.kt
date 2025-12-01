package org.firstinspires.ftc.teamcode.pedroPathing

import com.bylazar.configurables.annotations.Configurable
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import dev.nextftc.core.commands.groups.SequentialGroup
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.next.Commands.InitActuators
import org.firstinspires.ftc.teamcode.next.Commands.ShootBalls
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentHeading
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentX
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain.currentY
import org.firstinspires.ftc.teamcode.next.subsystems.Intake
import org.firstinspires.ftc.teamcode.next.subsystems.Limelight
import org.firstinspires.ftc.teamcode.next.subsystems.NewOuttake
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flap
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Hood
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Turret

@Autonomous
@Configurable
class ArtiFactAuto: NextFTCOpMode() {
    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(DriveTrain, Limelight, NewOuttake, Flywheel, Hood, Intake, InitActuators,
                Flap),
            BulkReadComponent,
            BindingsComponent,
        )
    }



    lateinit var poses: Far12
    override fun onInit() {
        follower.setStartingPose(Far12.start)
        telemetry.run {
            addData("follower X", follower.pose.x)
            addData("follower Y", follower.pose.y)
            addData("follower heading", follower.heading)
            addData("D0", Turret.getYaw())
            update()
        }
    }
    override fun onUpdate() {
        follower.pose = (Far12.start)
        telemetry.run {
            addData("follower X", currentX)
            addData("follower Y", currentY)
            addData("follower heading", currentHeading)
            addData("D0", NewOuttake.redGoalTag)
            update()
        }

    }
    override fun onStartButtonPressed() {
        //Turret.goToYaw(90.0).schedule()
        val p: Far12 = Far12(DriveTrain.alliance)
        SequentialGroup(
            p.ShootStart,
            ShootBalls(),

        ).schedule();
    }
}