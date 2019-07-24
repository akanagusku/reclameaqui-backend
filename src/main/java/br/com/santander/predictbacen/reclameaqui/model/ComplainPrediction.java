package br.com.santander.predictbacen.reclameaqui.model;

import br.com.santander.predictbacen.reclameaqui.database.MariaDbConnection;
import br.com.santander.predictbacen.reclameaqui.viewmodel.ComplainViewModel;
import org.joda.time.DateTime;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

//@Entity
//@Table(name = "predicted_score")
//@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
//		allowGetters = true)
public class ComplainPrediction
{

	public Integer id;
	public Double predictedScore;
	public Integer predictionModelId;
	public String complainId;
//	public DateTime createdAt;
	public Double hitPercentage;

	public Complain complain;
	public PredictionModel predictionModel;

	public ArrayList<ComplainPrediction> getByComplainId(String complainId) {
		String query = "SELECT * " +
				"FROM predicted_score " +
				"INNER JOIN complain_prediction_model ON predicted_score.prediction_model_id = complain_prediction_model.id " +
				"WHERE complain_id = '" + complainId +"' " +
				"ORDER BY complain_prediction_model.hit_percentage DESC";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<ComplainPrediction> complains = new ArrayList<>();

		try {

			ResultSet rs = statement.executeQuery(query);
			ComplainPrediction complainPrediction;
			while (rs.next())
			{
				complainPrediction = new ComplainPrediction();
				complainPrediction.predictionModel = new PredictionModel();
				complainPrediction.id = rs.getInt("id");
				complainPrediction.predictedScore = rs.getDouble("predicted_score");
				complainPrediction.predictionModelId = rs.getInt("prediction_model_id");
//				complainPrediction.createdAt = new DateTime(rs.getDate("created_at"));
				complainPrediction.complainId = rs.getString("complain_id");
				complainPrediction.predictionModel.modelName = rs.getString("model_name");
				complainPrediction.predictionModel.modelCategory = rs.getString("model_category");

				complains.add(complainPrediction);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return complains;

	}

	public void saveToDatabase(Double predictedScore, String predictionModelName, String complainId) {
		Statement bla = MariaDbConnection.getInstance().getStatement();
		try {
			String command = "INSERT INTO predicted_score(predicted_score, prediction_model_id, complain_id, created_at) VALUES ("+ predictedScore +", (SELECT id from complain_prediction_model where model_name = '" + predictionModelName + "'), '" + complainId + "', CURRENT_TIMESTAMP) ON DUPLICATE KEY UPDATE predicted_score = " + predictedScore + ";";
			bla.executeUpdate(command);
			System.out.println("Inserted predicted complain");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<ComplainPrediction> getOpenedPredictedComplain() {
		String query = "SELECT * FROM predicted_score INNER JOIN complain ON predicted_score.complain_id = complain.id where complain.score is not null AND hit_percentage is null";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<ComplainPrediction> complains = new ArrayList<>();

		try {

			ResultSet rs = statement.executeQuery(query);
			ComplainPrediction complainPrediction;
			while (rs.next())
			{
				complainPrediction = new ComplainPrediction();
				complainPrediction.complain = new Complain();
				complainPrediction.id = rs.getInt("id");
				complainPrediction.predictedScore = rs.getDouble("predicted_score");
				complainPrediction.predictionModelId = rs.getInt("prediction_model_id");
//				complainPrediction.createdAt = new DateTime(rs.getDate("created_at"));
				complainPrediction.complainId = rs.getString("complain_id");

				complainPrediction.complain.id = rs.getString("complain.id");
				complainPrediction.complain.score = rs.getDouble("complain.score");

				complains.add(complainPrediction);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return complains;
	}

	public void updatedFailPercentage(ArrayList<ComplainPrediction> openedPredictions) {
		for (ComplainPrediction currentPrediction: openedPredictions) {
			Statement bla = MariaDbConnection.getInstance().getStatement();
			try {
				String command = "UPDATE predicted_score SET hit_percentage= " + currentPrediction.hitPercentage + " WHERE id = " + currentPrediction.id + ";";
				bla.executeUpdate(command);
				System.out.println("Updated predicted complain");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public Integer getComplainPredictionsCount() {
		String query = "SELECT COUNT(id) FROM predicted_score";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		Integer predictionsCount = 0;

		try {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next())
			{
				predictionsCount = rs.getInt("COUNT(id)");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return predictionsCount;
	}
}