/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.ui.bricks;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.AllowedAfterDeadEndBrick;
import org.catrobat.catroid.content.bricks.ArduinoSendDigitalValueBrick;
import org.catrobat.catroid.content.bricks.ArduinoSendPWMValueBrick;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastReceiverBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.CameraBrick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ChangeVolumeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.ChooseCameraBrick;
import org.catrobat.catroid.content.bricks.ClearGraphicEffectBrick;
import org.catrobat.catroid.content.bricks.ComeToFrontBrick;
import org.catrobat.catroid.content.bricks.DeleteItemOfUserListBrick;
import org.catrobat.catroid.content.bricks.DroneEmergencyBrick;
import org.catrobat.catroid.content.bricks.DroneFlipBrick;
import org.catrobat.catroid.content.bricks.DroneMoveBackwardBrick;
import org.catrobat.catroid.content.bricks.DroneMoveBrick;
import org.catrobat.catroid.content.bricks.DroneMoveDownBrick;
import org.catrobat.catroid.content.bricks.DroneMoveForwardBrick;
import org.catrobat.catroid.content.bricks.DroneMoveLeftBrick;
import org.catrobat.catroid.content.bricks.DroneMoveRightBrick;
import org.catrobat.catroid.content.bricks.DroneMoveUpBrick;
import org.catrobat.catroid.content.bricks.DronePlayLedAnimationBrick;
import org.catrobat.catroid.content.bricks.DroneSwitchCameraBrick;
import org.catrobat.catroid.content.bricks.DroneTakeOffLandBrick;
import org.catrobat.catroid.content.bricks.DroneTurnLeftBrick;
import org.catrobat.catroid.content.bricks.DroneTurnLeftMagnetoBrick;
import org.catrobat.catroid.content.bricks.DroneTurnRightBrick;
import org.catrobat.catroid.content.bricks.DroneTurnRightMagnetoBrick;
import org.catrobat.catroid.content.bricks.FlashBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoNStepsBackBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.HideTextBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.catrobat.catroid.content.bricks.IfOnEdgeBounceBrick;
import org.catrobat.catroid.content.bricks.InsertItemIntoUserListBrick;
import org.catrobat.catroid.content.bricks.LegoNxtMotorMoveBrick;
import org.catrobat.catroid.content.bricks.LegoNxtMotorStopBrick;
import org.catrobat.catroid.content.bricks.LegoNxtMotorTurnAngleBrick;
import org.catrobat.catroid.content.bricks.LegoNxtPlayToneBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.LoopEndlessBrick;
import org.catrobat.catroid.content.bricks.MoveNStepsBrick;
import org.catrobat.catroid.content.bricks.NextLookBrick;
import org.catrobat.catroid.content.bricks.NoteBrick;
import org.catrobat.catroid.content.bricks.PhiroIfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.PhiroMotorMoveBackwardBrick;
import org.catrobat.catroid.content.bricks.PhiroMotorMoveForwardBrick;
import org.catrobat.catroid.content.bricks.PhiroMotorStopBrick;
import org.catrobat.catroid.content.bricks.PhiroPlayToneBrick;
import org.catrobat.catroid.content.bricks.PhiroRGBLightBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.content.bricks.RaspiIfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.RaspiPwmBrick;
import org.catrobat.catroid.content.bricks.RaspiSendDigitalValueBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.ReplaceItemInUserListBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetLookBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.SetVolumeToBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.SpeakBrick;
import org.catrobat.catroid.content.bricks.SpinnerBrick;
import org.catrobat.catroid.content.bricks.StopAllSoundsBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.TurnRightBrick;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.content.bricks.UserListBrick;
import org.catrobat.catroid.content.bricks.UserScriptDefinitionBrick;
import org.catrobat.catroid.content.bricks.VibrationBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.content.bricks.WhenBrick;
import org.catrobat.catroid.content.bricks.WhenRaspiPinChangedBrick;
import org.catrobat.catroid.content.bricks.WhenStartedBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.InterpretationException;
import org.catrobat.catroid.physics.content.bricks.CollisionReceiverBrick;
import org.catrobat.catroid.physics.content.bricks.SetBounceBrick;
import org.catrobat.catroid.physics.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.physics.content.bricks.SetGravityBrick;
import org.catrobat.catroid.physics.content.bricks.SetMassBrick;
import org.catrobat.catroid.physics.content.bricks.SetPhysicsObjectTypeBrick;
import org.catrobat.catroid.physics.content.bricks.SetVelocityBrick;
import org.catrobat.catroid.physics.content.bricks.TurnLeftSpeedBrick;
import org.catrobat.catroid.physics.content.bricks.TurnRightSpeedBrick;
import org.catrobat.catroid.ui.BrickView;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.catrobat.catroid.utils.Utils;

import static org.catrobat.catroid.content.bricks.Brick.BrickField;

public class BrickViewProvider {
	protected static final String TAG = "BrickViewFactory";
	protected Context context;
	protected LayoutInflater inflater;
	protected BrickViewOnClickDispatcher onClickDispatcher;
	private boolean noPuzzleViewEnabled;
	private boolean nextViewForLoopEndlessIsPuzzleView;
	private boolean prototypeLayout = false;

	public BrickViewProvider(Context context) {
		this(context, (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
	}

	protected BrickViewProvider(Context context, LayoutInflater inflater) {
		this.context = context;
		this.inflater = inflater;
		onClickDispatcher = new BrickViewOnClickDispatcher();
	}

	public void setPrototypeLayout(boolean prototypeLayout) {
		this.prototypeLayout = prototypeLayout;
	}

	public boolean isPrototypeLayout() {
		return prototypeLayout;
	}

	public void setNoPuzzleViewEnabled(boolean noPuzzleViewEnabled) {
		this.noPuzzleViewEnabled = noPuzzleViewEnabled;
	}

	public boolean isNoPuzzleViewEnabled() {
		return noPuzzleViewEnabled;
	}

	protected static class BrickViewOnClickDispatcher {
		public void dispatch(FormulaBrick brick, View source, Brick.BrickField brickfield) {
			FormulaEditorFragment.showFragment(source, brick, brickfield);
		}
	}

	protected static boolean clickAllowed(View view) {
		if (view instanceof BrickView) {
			BrickView brickView = (BrickView) view;
			if (brickView.getMode() != BrickView.Mode.DEFAULT) {
				return false;
			}
		}
		return true;
	}

	public BrickView createNoPuzzleView(final AllowedAfterDeadEndBrick brick, ViewGroup parent) {
		View view = null;

		if (noPuzzleViewEnabled) {
			if (brick instanceof LoopEndlessBrick) {
				view = createLoopEndlessBrickView(parent);
			} else if (brick instanceof LoopEndBrick) {
				view = createSimpleBrickView(parent, R.layout.brick_loop_end_no_puzzle);
			}
		}

		if (view == null) {
			view = createView(brick, parent);
		}
		return (BrickView) view;
	}

	public BrickView createView(final Brick brick, ViewGroup parent) {
		return createView(brick, parent, prototypeLayout);
	}

	public BrickView createView(final Brick brick, ViewGroup parent, boolean prototype) {
		View view = null;

		//Plain View
		if (brick instanceof LoopEndlessBrick) {
			view = createLoopEndlessBrickView(parent);
		} else if (brick instanceof ClearGraphicEffectBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_clear_graphic_effect);
		} else if (brick instanceof ComeToFrontBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_go_to_front);
		} else if (brick instanceof ForeverBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_forever);
		} else if (brick instanceof HideBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_hide);
		} else if (brick instanceof IfLogicElseBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_if_else);
		} else if (brick instanceof IfLogicEndBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_if_end_if);
		} else if (brick instanceof IfOnEdgeBounceBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_if_on_edge_bounce);
		} else if (brick instanceof LoopEndBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_loop_end);
		} else if (brick instanceof ShowBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_show);
		} else if (brick instanceof StopAllSoundsBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_stop_all_sounds);
		} else if (brick instanceof WhenBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_when);
		} else if (brick instanceof WhenStartedBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_when_started);
		} else if (brick instanceof NextLookBrick) {
			view = createSimpleBrickView(parent, R.layout.brick_next_look);
			if (ProjectManager.getInstance().getCurrentSprite().getName().equals(context.getString(R.string.background))) {
				((TextView) view.findViewById(R.id.brick_next_look_text_view)).setText(R.string.brick_next_background);
			}
		}

		//Single Formula Text
		else if (brick instanceof ChangeBrightnessByNBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_change_brightness,
					BrickField.BRIGHTNESS_CHANGE, R.id.brick_change_brightness_edit_text);
		} else if (brick instanceof ChangeTransparencyByNBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_change_transparency,
					BrickField.TRANSPARENCY_CHANGE, R.id.brick_change_transparency_edit_text);
		} else if (brick instanceof ChangeSizeByNBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_change_size_by_n,
					BrickField.SIZE_CHANGE, R.id.brick_change_size_by_edit_text);
		} else if (brick instanceof ChangeVolumeByNBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_change_volume_by,
					BrickField.VOLUME_CHANGE, R.id.brick_change_volume_by_edit_text);
		} else if (brick instanceof ChangeXByNBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_change_x,
					BrickField.X_POSITION_CHANGE, R.id.brick_change_x_edit_text);
		} else if (brick instanceof ChangeYByNBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_change_y,
					BrickField.Y_POSITION_CHANGE, R.id.brick_change_y_edit_text);
		} else if (brick instanceof IfLogicBeginBrick && !(brick instanceof PhiroIfLogicBeginBrick)) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_if_begin_if,
					BrickField.IF_CONDITION, R.id.brick_if_begin_edit_text);
		} else if (brick instanceof NoteBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_note,
					BrickField.NOTE, R.id.brick_note_edit_text);
		} else if (brick instanceof PointInDirectionBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_point_in_direction,
					BrickField.DEGREES, R.id.brick_point_in_direction_edit_text);
		} else if (brick instanceof SetBrightnessBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_set_brightness,
					BrickField.BRIGHTNESS, R.id.brick_set_brightness_edit_text);
		} else if (brick instanceof SetTransparencyBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_set_transparency,
					BrickField.TRANSPARENCY, R.id.brick_set_transparency_to_edit_text);
		} else if (brick instanceof SetSizeToBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_set_size_to,
					BrickField.SIZE, R.id.brick_set_size_to_edit_text);
		} else if (brick instanceof SetVolumeToBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_set_volume_to,
					BrickField.VOLUME, R.id.brick_set_volume_to_edit_text);
		} else if (brick instanceof SetXBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_set_x,
					BrickField.X_POSITION, R.id.brick_set_x_edit_text);
		} else if (brick instanceof SetYBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_set_y,
					BrickField.Y_POSITION, R.id.brick_set_y_edit_text);
		} else if (brick instanceof SpeakBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_speak,
					BrickField.SPEAK, R.id.brick_speak_edit_text);
		} else if (brick instanceof TurnLeftBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_turn_left,
					BrickField.TURN_LEFT_DEGREES, R.id.brick_turn_left_edit_text);
		} else if (brick instanceof TurnRightBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_turn_right,
					BrickField.TURN_RIGHT_DEGREES, R.id.brick_turn_right_edit_text);
		}

		//Single Formula Text with plural text
		else if (brick instanceof MoveNStepsBrick) {
			view = createSingleFormulaBrickViewWithPluralText((FormulaBrick) brick, parent, R.layout.brick_move_n_steps,
					BrickField.STEPS, R.id.brick_move_n_steps_edit_text,
					R.id.brick_move_n_steps_step_text_view, R.plurals.brick_move_n_step_plural);
		} else if (brick instanceof GoNStepsBackBrick) {
			view = createSingleFormulaBrickViewWithPluralText((FormulaBrick) brick, parent, R.layout.brick_go_back,
					BrickField.STEPS, R.id.brick_go_back_edit_text,
					R.id.brick_go_back_layers_text_view, R.plurals.brick_go_back_layer_plural);
		} else if (brick instanceof RepeatBrick) {
			view = createSingleFormulaBrickViewWithPluralText((FormulaBrick) brick, parent, R.layout.brick_repeat,
					BrickField.TIMES_TO_REPEAT, R.id.brick_repeat_edit_text,
					R.id.brick_repeat_time_text_view, R.plurals.time_plural);
		} else if (brick instanceof VibrationBrick) {
			view = createSingleFormulaBrickViewWithPluralText((FormulaBrick) brick, parent, R.layout.brick_vibration,
					BrickField.VIBRATE_DURATION_IN_SECONDS, R.id.brick_vibration_edit_text,
					R.id.brick_vibration_second_label, R.plurals.second_plural);
		} else if (brick instanceof WaitBrick) {
			view = createSingleFormulaBrickViewWithPluralText((FormulaBrick) brick, parent, R.layout.brick_wait,
					BrickField.TIME_TO_WAIT_IN_SECONDS, R.id.brick_wait_edit_text,
					R.id.brick_wait_second_text_view, R.plurals.second_plural);
		}

		//Double Formula Text
		else if (brick instanceof PlaceAtBrick) {
			view = createDoubleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_place_at,
					BrickField.X_POSITION, BrickField.Y_POSITION, R.id.brick_place_at_edit_text_x,
					R.id.brick_place_at_edit_text_y);
		}

		//DroneBrick Views
		else if (brick instanceof DroneFlipBrick) {
			view = createSimpleDroneBrickView(parent, ((DroneFlipBrick) brick).getBrickLabel());
		} else if (brick instanceof DronePlayLedAnimationBrick) {
			view = createSimpleDroneBrickView(parent, ((DronePlayLedAnimationBrick) brick).getBrickLabel());
		} else if (brick instanceof DroneTakeOffLandBrick) {
			view = createSimpleDroneBrickView(parent, ((DroneTakeOffLandBrick) brick).getBrickLabel());
		} else if (brick instanceof DroneEmergencyBrick) {
			view = createSimpleDroneBrickView(parent, ((DroneEmergencyBrick) brick).getBrickLabel());
		} else if (brick instanceof DroneSwitchCameraBrick) {
			view = createSimpleDroneBrickView(parent, ((DroneSwitchCameraBrick) brick).getBrickLabel());
		} else if (brick instanceof DroneMoveBackwardBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_move_backward);
		} else if (brick instanceof DroneMoveDownBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_move_down);
		} else if (brick instanceof DroneMoveForwardBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_move_forward);
		} else if (brick instanceof DroneMoveLeftBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_move_left);
		} else if (brick instanceof DroneMoveRightBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_move_right);
		} else if (brick instanceof DroneMoveUpBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_move_up);
		} else if (brick instanceof DroneTurnLeftBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_turn_left);
		} else if (brick instanceof DroneTurnLeftMagnetoBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_turn_left_magneto);
		} else if (brick instanceof DroneTurnRightBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_turn_right);
		} else if (brick instanceof DroneTurnRightMagnetoBrick) {
			view = createDroneMoveBrickView((DroneMoveBrick) brick, parent, R.string.brick_drone_turn_right_magneto);
		}

		//LegoBrick Views
		else if (brick instanceof LegoNxtPlayToneBrick) {
			view = createDoubleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_nxt_play_tone,
					BrickField.LEGO_NXT_DURATION_IN_SECONDS, BrickField.LEGO_NXT_FREQUENCY, R.id.nxt_tone_duration_edit_text,
					R.id.nxt_tone_freq_edit_text);
		} else if (brick instanceof LegoNxtMotorTurnAngleBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, BrickField.LEGO_NXT_DEGREES, R.id.motor_turn_angle_edit_text);
		} else if (brick instanceof LegoNxtMotorMoveBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, BrickField.LEGO_NXT_SPEED, R.id.motor_action_speed_edit_text);
		} else if (brick instanceof LegoNxtMotorStopBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, null, 0);
		}

		//Arduino
		else if (brick instanceof ArduinoSendDigitalValueBrick) {
			view = createDoubleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_arduino_send_digital,
					BrickField.ARDUINO_DIGITAL_PIN_NUMBER, BrickField.ARDUINO_DIGITAL_PIN_VALUE, R.id.brick_arduino_set_digital_pin_edit_text,
					R.id.brick_arduino_set_digital_value_edit_text);
		} else if (brick instanceof ArduinoSendPWMValueBrick) {
			view = createDoubleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_arduino_send_analog,
					BrickField.ARDUINO_ANALOG_PIN_NUMBER, BrickField.ARDUINO_ANALOG_PIN_VALUE, R.id.brick_arduino_set_analog_pin_edit_text,
					R.id.brick_arduino_set_analog_value_edit_text);
		}

		//RaspberryPI Views
		else if (brick instanceof RaspiIfLogicBeginBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_raspi_if_begin_if,
					BrickField.IF_CONDITION, R.id.brick_raspi_if_begin_edit_text);
		} else if (brick instanceof RaspiSendDigitalValueBrick) {
			view = createDoubleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_raspi_send_digital,
					BrickField.RASPI_DIGITAL_PIN_NUMBER, BrickField.RASPI_DIGITAL_PIN_VALUE, R.id.brick_raspi_set_digital_pin_edit_text,
					R.id.brick_raspi_set_digital_value_edit_text);
		} else if (brick instanceof RaspiPwmBrick) {
			view =  createRaspiPwmBrick((RaspiPwmBrick) brick, parent);
		} else if (brick instanceof WhenRaspiPinChangedBrick) {
			WhenRaspiPinChangedBrickViewProvider factory = new WhenRaspiPinChangedBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createWhenRaspiPinChangedBrickView((WhenRaspiPinChangedBrick) brick, parent);
		}

		//Phiro Views
		else if (brick instanceof PhiroIfLogicBeginBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, null, 0);
		} else if (brick instanceof PhiroMotorMoveForwardBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, BrickField.PHIRO_SPEED, R.id
					.brick_phiro_motor_forward_action_speed_edit_text);
		} else if (brick instanceof PhiroMotorMoveBackwardBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, BrickField.PHIRO_SPEED,
					R.id.brick_phiro_motor_backward_action_speed_edit_text);
		}  else if (brick instanceof PhiroMotorStopBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, null, 0);
		}  else if (brick instanceof PhiroPlayToneBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, BrickField.PHIRO_DURATION_IN_SECONDS,
					R.id.brick_phiro_play_tone_duration_edit_text);
		} else if (brick instanceof PhiroRGBLightBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, BrickField.PHIRO_LIGHT_RED,
					R.id.brick_phiro_rgb_led_action_red_edit_text);
			initFormulaEditView((FormulaBrick)brick, view, BrickField.PHIRO_LIGHT_GREEN, R.id
					.brick_phiro_rgb_led_action_green_edit_text);
			initFormulaEditView((FormulaBrick)brick, view, BrickField.PHIRO_LIGHT_BLUE, R.id
					.brick_phiro_rgb_led_action_blue_edit_text);
		}

		//Physics Views
		else if (brick instanceof CollisionReceiverBrick) {
			CollisionReceiverBrickViewProvider factory = new CollisionReceiverBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createCollisionReceiverBrickView((CollisionReceiverBrick) brick, parent);
		} else if (brick instanceof SetBounceBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout
					.brick_physics_set_bounce_factor, BrickField.PHYSICS_BOUNCE_FACTOR, R.id
					.brick_set_bounce_factor_edit_text);
		} else if (brick instanceof SetFrictionBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout
					.brick_physics_set_friction, BrickField.PHYSICS_FRICTION, R.id
					.brick_set_friction_edit_text);
		} else if (brick instanceof SetGravityBrick) {
			view = createDoubleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_physics_set_gravity,
					BrickField.PHYSICS_GRAVITY_X, BrickField.PHYSICS_GRAVITY_Y, R.id.brick_set_gravity_edit_text_x,
					R.id.brick_set_gravity_edit_text_y);
		} else if (brick instanceof SetMassBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout
					.brick_physics_set_mass, BrickField.PHYSICS_MASS, R.id
					.brick_set_mass_edit_text);
		} else if(brick instanceof SetPhysicsObjectTypeBrick) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, null, 0);
		} else if (brick instanceof SetVelocityBrick) {
			view = createDoubleFormulaBrickView((FormulaBrick) brick, parent, R.layout.brick_physics_set_velocity,
					BrickField.PHYSICS_VELOCITY_X, BrickField.PHYSICS_VELOCITY_Y, R.id.brick_set_velocity_edit_text_x,
					R.id.brick_set_velocity_edit_text_y);
		} else if (brick instanceof TurnLeftSpeedBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout
					.brick_physics_turn_left_speed, BrickField.PHYSICS_TURN_LEFT_SPEED, R.id
					.brick_turn_left_speed_edit_text);
		} else if (brick instanceof TurnRightSpeedBrick) {
			view = createSingleFormulaBrickView((FormulaBrick) brick, parent, R.layout
					.brick_physics_turn_right_speed, BrickField.PHYSICS_TURN_RIGHT_SPEED, R.id
					.brick_turn_right_speed_edit_text);
		}

		//CustomUI
		else if (brick instanceof GlideToBrick) {
			view = createGlideToBrick((GlideToBrick) brick, parent);
		} else if (brick instanceof AddItemToUserListBrick) {
			ManipulateItemOfUserListBrickViewProvider factory = new ManipulateItemOfUserListBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createManipulateItemToUserListBrickView((UserListBrick) brick, parent, ManipulateItemOfUserListBrickViewProvider.ManipulationMode.ADD);
		} else if (brick instanceof DeleteItemOfUserListBrick) {
			ManipulateItemOfUserListBrickViewProvider factory = new ManipulateItemOfUserListBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createManipulateItemToUserListBrickView((UserListBrick) brick, parent, ManipulateItemOfUserListBrickViewProvider.ManipulationMode.DELETE);
		} else if (brick instanceof InsertItemIntoUserListBrick) {
			ManipulateItemOfUserListBrickViewProvider factory = new ManipulateItemOfUserListBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createManipulateItemToUserListBrickView((UserListBrick) brick, parent, ManipulateItemOfUserListBrickViewProvider.ManipulationMode.INSERT);
		} else if (brick instanceof ReplaceItemInUserListBrick) {
			ManipulateItemOfUserListBrickViewProvider factory = new ManipulateItemOfUserListBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createManipulateItemToUserListBrickView((UserListBrick) brick, parent, ManipulateItemOfUserListBrickViewProvider.ManipulationMode.REPLACE);
		} else if (brick instanceof ChangeVariableBrick) {
			ChangeVariableBrickViewProvider factory = new ChangeVariableBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createChangeVariableBrickView((ChangeVariableBrick) brick, parent, false);
		} else if (brick instanceof SetVariableBrick) {
			ChangeVariableBrickViewProvider factory = new ChangeVariableBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createChangeVariableBrickView((SetVariableBrick) brick, parent, true);
		} else if (brick instanceof BroadcastWaitBrick) {
			BroadcastWaitBrickViewProvider factory = new BroadcastWaitBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createBroadcastWaitBrickView((BroadcastWaitBrick) brick, parent);
		} else if (brick instanceof BroadcastBrick) {
			BroadcastBrickViewProvider factory = new BroadcastBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createBroadcastBrickView((BroadcastBrick) brick, parent);
		} else if (brick instanceof BroadcastReceiverBrick) {
			BroadcastReceiverBrickViewProvider factory = new BroadcastReceiverBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createBroadcastReceiverBrickView((BroadcastReceiverBrick) brick, parent);
		} else if (brick instanceof PlaySoundBrick) {
			PlaySoundBrickViewProvider factory = new PlaySoundBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createPlaySoundBrickView((PlaySoundBrick) brick, parent);
		} else if (brick instanceof PointToBrick) {
			PointToBrickViewProvider factory = new PointToBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createPointToBrickView((PointToBrick) brick, parent);
		} else if (brick instanceof SetLookBrick) {
			SetLookBrickViewProvider factory = new SetLookBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createSetLookBrickView((SetLookBrick) brick, parent);
		} /*else if (brick instanceof UserBrick) {
			UserBrickViewProvider factory = new UserBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createUserBrickView((UserBrick) brick, parent);
		}*/ else if (brick instanceof UserScriptDefinitionBrick) {
			UserScriptDefinitionBrickViewProvider factory = new UserScriptDefinitionBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototypeLayout);
			view = factory.createUserScriptDefinitionBrickView((UserScriptDefinitionBrick) brick, parent);
		} else if ((brick instanceof CameraBrick) || (brick instanceof ChooseCameraBrick) || (brick instanceof FlashBrick)) {
			view = createSpinnerBrickViewWithFormula((SpinnerBrick) brick, parent, null, 0);
		} else if (brick instanceof ShowTextBrick) {
			HideShowTextBrickViewProvider factory = new HideShowTextBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototype);
			view = factory.createShowTextBrickView((ShowTextBrick) brick, parent);
		} else if (brick instanceof HideTextBrick) {
			HideShowTextBrickViewProvider factory = new HideShowTextBrickViewProvider(context, inflater);
			factory.setPrototypeLayout(prototype);
			view = factory.createHideTextBrickView((HideTextBrick) brick, parent);
		} else {
			Log.d("BRICKVIEWPROVIDER", brick.getClass().getName() + " not in IF ELSE");
		}

		return (BrickView) view;
	}

	private View createLoopEndlessBrickView(ViewGroup parent) {
		int layout;
		if (nextViewForLoopEndlessIsPuzzleView) {
			nextViewForLoopEndlessIsPuzzleView = false;
			layout = R.layout.brick_loop_endless_no_puzzle;
		} else {
			nextViewForLoopEndlessIsPuzzleView = true;
			layout = R.layout.brick_loop_endless;
		}
		return createSimpleBrickView(parent, layout);
	}

	private Resources getResources() {
		return context.getResources();
	}

	protected View inflateBrickView(final ViewGroup parent, int layoutResId) {
		return inflater.inflate(layoutResId, parent, false);
	}

	protected View createSimpleBrickView(final ViewGroup parent, int layoutResId) {
		return inflateBrickView(parent, layoutResId);
	}

	protected View createSingleFormulaBrickView(final FormulaBrick brick, final ViewGroup parent,
			int layoutResId, final BrickField brickField, int formula1ViewId) {

		final View view = inflateBrickView(parent, layoutResId);
		initFormulaEditView(brick, view, brickField, formula1ViewId);
		return view;
	}

	private View createSimpleDroneBrickView(final ViewGroup parent, int stringResId) {
		parent.getResources().getString(stringResId);
		View inflated = inflateBrickView(parent, R.layout.brick_drone);
		TextView text = (TextView) inflated.findViewById(R.id.brick_drone_label);
		text.setText(stringResId);
		return inflated;
	}

	private View createDroneMoveBrickView(DroneMoveBrick brick, ViewGroup parent, int labelStringId) {
		final View view = inflateBrickView(parent, R.layout.brick_drone_move);

		((TextView) view.findViewById(R.id.brick_drone_move_label)).setText(getResources().getString(labelStringId));
		initFormulaEditView(brick, view, BrickField.DRONE_POWER_IN_PERCENT, R.id.brick_drone_move_edit_text_power);

		Formula formula = initFormulaEditView(brick, view, BrickField.DRONE_TIME_TO_FLY_IN_SECONDS, R.id.brick_drone_move_edit_text_second);
		setPluralText(brick, view, formula, R.id.brick_drone_move_text_view_second, R.plurals.second_plural);

		return view;
	}

	private View createSingleFormulaBrickViewWithPluralText(final FormulaBrick brick, final ViewGroup parent,
			int layoutResId, final BrickField brickField, int formula1ViewId, int text1ViewId, int pluralStringId) {

		final View view = inflateBrickView(parent, layoutResId);
		Formula formula = initFormulaEditView(brick, view, brickField, formula1ViewId);
		setPluralText(brick, view, formula, text1ViewId, pluralStringId);

		return view;
	}

	private void setPluralText(Brick brick, View view, Formula formula, int textResId, int pluralStringId) {
		TextView textView = (TextView) view.findViewById(textResId);

		if (formula.isSingleNumberFormula()) {
			try {
				textView.setText(getResources().getQuantityString(pluralStringId,
						Utils.convertDoubleToPluralInteger(formula.interpretDouble(ProjectManager.getInstance().getCurrentSprite()))
				));
			} catch (InterpretationException interpretationException) {
				Log.d(TAG, "Formula interpretation for this specific Brick " + brick.getClass().getSimpleName() + " failed.", interpretationException);
			}
		} else {
			textView.setText(getResources().getQuantityString(pluralStringId, Utils.TRANSLATION_PLURAL_OTHER_INTEGER));
		}
	}

	private View createDoubleFormulaBrickView(final FormulaBrick brick, final ViewGroup parent,
			int layoutResId, final BrickField brickField1, final BrickField brickField2, int formula1ViewId, int formula2ViewId) {
		final View view = inflateBrickView(parent, layoutResId);

		initFormulaEditView(brick, view, brickField1, formula1ViewId);
		initFormulaEditView(brick, view, brickField2, formula2ViewId);

		return view;
	}

	protected Formula initFormulaEditView(final FormulaBrick brick, final View view, final BrickField brickField, int fieldResId) {

		TextView editView = (TextView) view.findViewById(fieldResId);
		Formula formula = brick.getFormulaWithBrickField(brickField);
		formula.setTextFieldId(fieldResId);
		formula.refreshTextField(view);
		editView.setVisibility(View.VISIBLE);

		if (!isPrototypeLayout()) {
			editView.setEnabled(true);
			editView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View source) {
					if (clickAllowed(view)) {
						onClickDispatcher.dispatch(brick, source, brickField);
					}
				}
			});
		}
		return formula;
	}

	private View createGlideToBrick(final GlideToBrick brick, ViewGroup parent) {
		final View view = inflateBrickView(parent, R.layout.brick_glide_to);

		initFormulaEditView(brick, view, BrickField.X_DESTINATION, R.id.brick_glide_to_edit_text_x);
		initFormulaEditView(brick, view, BrickField.Y_DESTINATION, R.id.brick_glide_to_edit_text_y);

		Formula durationInSecondsFormula = initFormulaEditView(brick, view, BrickField.DURATION_IN_SECONDS, R.id.brick_glide_to_edit_text_duration);
		setPluralText(brick, view, durationInSecondsFormula, R.id.brick_glide_to_seconds_text_view, R.plurals.second_plural);

		return view;
	}

	private View createRaspiPwmBrick(final RaspiPwmBrick brick, ViewGroup parent) {
		final View view = inflateBrickView(parent, R.layout.brick_raspi_pwm);

		initFormulaEditView(brick, view, BrickField.RASPI_DIGITAL_PIN_NUMBER, R.id.brick_raspi_pwm_pin_edit_text);
		initFormulaEditView(brick, view, BrickField.RASPI_PWM_FREQUENCY, R.id.brick_raspi_pwm_frequency_edit_text);
		initFormulaEditView(brick, view, BrickField.RASPI_PWM_PERCENTAGE, R.id.brick_raspi_pwm_percentage_edit_text);

		return view;
	}

	private View createSpinnerBrickViewWithFormula(final SpinnerBrick brick, ViewGroup parent, final BrickField brickField, int fieldResId) {

		final View view = inflateBrickView(parent, brick.getLayoutId());

		if (brickField != null) {
			initFormulaEditView((FormulaBrick) brick, view, brickField, fieldResId);
		}

		ArrayAdapter<CharSequence> dataAdapter = brick.createArrayAdapter(context);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final Spinner spinner = (Spinner) view.findViewById(brick.getSpinnerId());

		spinner.setFocusableInTouchMode(false);
		spinner.setFocusable(false);
		spinner.setAdapter(dataAdapter);
		if (isPrototypeLayout()) {
			spinner.setEnabled(false);
			spinner.setClickable(false);
		} else {
			spinner.setEnabled(true);
			spinner.setClickable(true);
			spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
					brick.setSelectedSpinnerItem(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {

				}

			});
		}

		spinner.setSelection(brick.getSelectedSpinnerItem());
		return view;
	}
}