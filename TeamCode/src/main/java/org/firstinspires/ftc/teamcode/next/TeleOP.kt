package org.firstinspires.ftc.teamcode.next

import com.bylazar.telemetry.JoinedTelemetry
import com.bylazar.telemetry.PanelsTelemetry
import com.pedropathing.geometry.Pose
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import dev.nextftc.core.commands.utility.LambdaCommand
import dev.nextftc.core.components.BindingsComponent
import dev.nextftc.core.components.SubsystemComponent
import dev.nextftc.extensions.pedro.PedroComponent
import dev.nextftc.ftc.NextFTCOpMode
import dev.nextftc.ftc.components.BulkReadComponent
import org.firstinspires.ftc.teamcode.next.subsystems.DriveTrain
import org.firstinspires.ftc.teamcode.next.subsystems.Limelight
import org.firstinspires.ftc.teamcode.next.subsystems.NewOuttake
import org.firstinspires.ftc.teamcode.pedroPathing.Constants
import dev.nextftc.extensions.pedro.PedroComponent.Companion.follower
import dev.nextftc.ftc.Gamepads
import org.firstinspires.ftc.teamcode.next.Commands.InitActuators
import org.firstinspires.ftc.teamcode.next.Commands.ShootBalls
import org.firstinspires.ftc.teamcode.next.subsystems.Intake
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flap
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Flywheel
import org.firstinspires.ftc.teamcode.next.subsystems.outtake.Hood


@TeleOp(name = "DFF")
class TeleOP: NextFTCOpMode() {
    var tele = JoinedTelemetry(PanelsTelemetry.ftcTelemetry, telemetry)

    init {
        addComponents(
            PedroComponent(Constants::createFollower),
            SubsystemComponent(DriveTrain, Limelight, NewOuttake, Flywheel, Hood, Intake, Flap,
                InitActuators),
            BulkReadComponent,
            BindingsComponent,
        )
    }

    override fun onInit() {
        InitActuators.init()// Or a LambdaCommand
        Gamepads.gamepad1.a whenBecomesTrue ShootBalls();
        Gamepads.gamepad1.rightTrigger.greaterThan(0.3) whenBecomesTrue Intake.runIntake whenBecomesFalse Intake.stopIntake

    }

    override fun onStartButtonPressed() {
        InitActuators.init()// Or a LambdaCommand
    }

    override fun onUpdate() {

        tele.run {
            try {
                val current = Limelight.megaTag()
                if (current != null) {
                    addData("heading", current)

                    if(follower.pose != null){
                        follower.pose = current;
                    }
                }
            }
            catch (e: Error){

            }

            update()
        }
    }
}

