package br.com.santander.predictbacen.reclameaqui.model;

import br.com.santander.predictbacen.reclameaqui.database.MariaDbConnection;
import br.com.santander.predictbacen.reclameaqui.viewmodel.ComplainViewModel;
import br.com.santander.predictbacen.reclameaqui.viewmodel.PredictionModelViewModel;
import br.com.santander.predictbacen.reclameaqui.viewmodel.QuantityPredictionModelCompetition;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PredictionModel
{
	public Integer id;
	public String modelName;
	public String modelCategory;
	public String path;
	public Double meanResidualDeviance;
	public Integer priority;
	public Double hitPercentage;

	public void saveToDatabase(String modelName, String modelCategory, String path) {
		Statement bla = MariaDbConnection.getInstance().getStatement();
		try {
			String command = "INSERT INTO complain_prediction_model(model_name, model_category, path) VALUES ('"+ modelName +"', '" + modelCategory + "', '" + path + "') ON DUPLICATE KEY UPDATE model_category = '" + modelCategory + "';";
			bla.executeUpdate(command);
			System.out.println("Inserted model");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<ComplainViewModel> getPredictionsByModelId(Integer id) {
		String query = "SELECT * " +
				"FROM predicted_score " +
				"INNER JOIN complain ON predicted_score.complain_id = complain.id " +
				"WHERE prediction_model_id = " + id + "";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<ComplainViewModel> complains = new ArrayList<>();

		try {

			ResultSet rs = statement.executeQuery(query);
			ComplainViewModel complainPrediction;
			while (rs.next())
			{
				complainPrediction = new ComplainViewModel();
				complainPrediction.predictedScore = rs.getDouble("predicted_score");
				complainPrediction.title = rs.getString("title");
				complainPrediction.description = rs.getString("description");
//				complainPrediction.createdAt = new DateTime(rs.getDate("created_at"));
				complainPrediction.userState = rs.getString("userState");
				complainPrediction.userCity = rs.getString("userCity");

				complains.add(complainPrediction);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return complains;
	}

	public ArrayList<PredictionModelViewModel> findAllActive() {
		String query = "SELECT * FROM complain_prediction_model where is_active = true ORDER BY hit_percentage DESC";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<PredictionModelViewModel> predictionModels = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery(query);
			PredictionModelViewModel predictionModel;
			while (rs.next())
			{
				predictionModel = new PredictionModelViewModel();
				predictionModel.id = rs.getInt("id");
				predictionModel.modelName = rs.getString("model_name");
				predictionModel.modelCategory = rs.getString("model_category");
				predictionModel.path = rs.getString("path");
				predictionModel.meanResidualDeviance = rs.getDouble("mean_residual_deviance");
				predictionModel.priority = rs.getInt("priority");
				predictionModel.hitPercentage = rs.getDouble("hit_percentage");

				predictionModels.add(predictionModel);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return predictionModels;
	}

	public void updateHitPercentage() {
		String query = "UPDATE complain_prediction_model \n" +
				"INNER JOIN predicted_score ON complain_prediction_model.id = predicted_score.prediction_model_id \n" +
				"SET complain_prediction_model.hit_percentage = (SELECT AVG(predicted_score.hit_percentage) FROM predicted_score WHERE predicted_score.prediction_model_id = complain_prediction_model.id)";
		Statement statement = MariaDbConnection.getInstance().getStatement();

		try {
			statement.executeQuery(query);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public PredictionModel getChampion() {
		String query = "SELECT * FROM complain_prediction_model ORDER BY hit_percentage DESC LIMIT 1";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		PredictionModel champion = new PredictionModel();

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				champion.hitPercentage = rs.getDouble("hit_percentage");
				champion.modelCategory = rs.getString("hit_percentage");
				champion.modelName = rs.getString("model_name");
				champion.id = rs.getInt("id");
				champion.meanResidualDeviance = rs.getDouble("mean_residual_deviance");
				champion.path = rs.getString("path");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return champion;
	}

	public ArrayList<QuantityPredictionModelCompetition> getModelCompetition() {
		String query = "SELECT COUNT(a.id), a.prediction_model_id, complain_prediction_model.model_name \n" +
				"FROM predicted_score a\n" +
				"INNER JOIN (\n" +
				"    SELECT complain_id, MAX(hit_percentage) hit_percentage \n" +
				"    FROM predicted_score \n" +
				"    GROUP BY complain_id \n" +
				") b ON a.complain_id = b.complain_id AND a.hit_percentage = b.hit_percentage \n" +
				"INNER JOIN complain_prediction_model ON \n" +
				"a.prediction_model_id = complain_prediction_model.id \n" +
				"WHERE a.hit_percentage is not null \n" +
				"GROUP BY a.prediction_model_id";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<QuantityPredictionModelCompetition> predictionModels = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery(query);
			QuantityPredictionModelCompetition predictionModel;
			while (rs.next())
			{
				predictionModel = new QuantityPredictionModelCompetition();
				predictionModel.quantity = rs.getInt("COUNT(a.id)");
				predictionModel.modelName = rs.getString("complain_prediction_model.model_name");
				predictionModel.modelId = rs.getInt("prediction_model_id");

				predictionModels.add(predictionModel);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return predictionModels;
	}
}