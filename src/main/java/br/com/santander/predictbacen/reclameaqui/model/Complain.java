package br.com.santander.predictbacen.reclameaqui.model;

import br.com.santander.predictbacen.reclameaqui.database.MariaDbConnection;
import br.com.santander.predictbacen.reclameaqui.nlp.CogrooAnalyzer;
import br.com.santander.predictbacen.reclameaqui.nlp.NaturalLanguageProcessingUtil;
import br.com.santander.predictbacen.reclameaqui.viewmodel.QuantityByMonthYear;
import br.com.santander.predictbacen.reclameaqui.viewmodel.QuantityByStateViewModel;
import br.com.santander.predictbacen.reclameaqui.viewmodel.QuantityDateDiff;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

//@Entity
//@Table(name = "complain")
//@EntityListeners(AuditingEntityListener.class)
//@JsonIgnoreProperties(value = {"createdAt", "updatedAt"},
//		allowGetters = true)
public class Complain 
{
	private static final String SEPARATOR = "Æ";

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public String id;

//	@NotBlank
	public String title;
	public String description;
	public String complainOrigin;
	public String userState;
	public String userCity;
	public String status;
	public DateTime created;
	public DateTime modified;
	public String additionalFields;
	public String moderationUserName;
	public String otherProblemType;
	public String solved;
	public Integer dealAgain;
	public String otherProductType;
	public String type;
	public String evaluation;
	public Double score;
	public Integer userRequestedDelete;
	public String requesterName;
	public String additionalInfo;
	public Integer evaluated;
	public Integer publishedEmailSent;
	public String deletionReason;
	public Integer read;
	public String marketplaceComplain;
	public String ip;
	public String moderationReasonDescription;
	public Integer frozen;
	public DateTime requestEvaluation;
	public String moderateReason;
	public DateTime firstInteractionDate;
	public Integer hasReply;
	public Integer compliment;
	public String legacyId;
	public String deletedIp;
	public Integer indexable;
	public Integer hasBacen;
	public Integer hasProcess;
	public Integer hasScore;
	public Integer hasFirstInteraction;
	public Integer timeToFirstInteractionGreaterThan0;
	public Integer timeToFirstInteractionGreaterThan1;
	public Integer timeToFirstInteractionGreaterThan2;
	public Integer timeToFirstInteractionGreaterThan3;
	public Integer timeToFirstInteractionGreaterThan4;
	public Integer timeToFirstInteractionGreaterThan5;
	public Integer timeToFirstInteractionGreaterThan6;
	public Integer hasQuestion;
	public String complainPeriod;
	public Integer hasEditadoPeloReclameAqui;
	public Integer hasProcon;
	public Integer verboReclamar;
	public Integer verboProcessar;

	public Double predictedScore;
	
	public static String getComplainHeader(String separator)
	{
		if (separator.isEmpty()) {
			separator = SEPARATOR;
		}

		StringBuilder sB = new StringBuilder();
		sB.append("id").append(separator);
		sB.append("title").append(separator);
		sB.append("description").append(separator);
		sB.append("complainOrigin").append(separator);
		sB.append("userState").append(separator);
		sB.append("userCity").append(separator);
		sB.append("status").append(separator);
		sB.append("created").append(separator);
		sB.append("modified").append(separator);
		sB.append("additionalFields").append(separator);
		sB.append("moderationUserName").append(separator);
		sB.append("otherProblemType").append(separator);
		sB.append("solved").append(separator);
		sB.append("dealAgain").append(separator);
		sB.append("otherProductType").append(separator);
		sB.append("type").append(separator);
		sB.append("evaluation").append(separator);
		sB.append("score").append(separator);
		sB.append("userRequestedDelete").append(separator);
		sB.append("requesterName").append(separator);
		sB.append("additionalInfo").append(separator);
		sB.append("evaluated").append(separator);
		sB.append("publishedEmailSent").append(separator);
		sB.append("deletionReason").append(separator);
		sB.append("read").append(separator);
		sB.append("marketplaceComplain").append(separator);
		sB.append("ip").append(separator);
		sB.append("moderationReasonDescription").append(separator);
		sB.append("frozen").append(separator);
		sB.append("requestEvaluation").append(separator);
		sB.append("moderateReason").append(separator);
		sB.append("firstInteractionDate").append(separator);
		sB.append("hasReply").append(separator);
		sB.append("compliment").append(separator);
		sB.append("legacyId").append(separator);
		sB.append("deletedIp").append(separator);
		sB.append("indexable").append(separator);
		sB.append("hasBacen").append(separator);
		sB.append("hasProcess").append(separator);
		sB.append("hasScore").append(separator);
		sB.append("hasFirstInteraction").append(separator);
		sB.append("timeToFirstInteractionGreaterThan0").append(separator);
		sB.append("timeToFirstInteractionGreaterThan1").append(separator);
		sB.append("timeToFirstInteractionGreaterThan2").append(separator);
		sB.append("timeToFirstInteractionGreaterThan3").append(separator);
		sB.append("timeToFirstInteractionGreaterThan4").append(separator);
		sB.append("timeToFirstInteractionGreaterThan5").append(separator);
		sB.append("timeToFirstInteractionGreaterThan6").append(separator);
		sB.append("hasQuestion").append(separator);
		sB.append("complainPeriod").append(separator);
		sB.append("hasEditadoPeloReclameAqui").append(separator);
		sB.append("hasProcon").append(separator);
		sB.append("verboReclamar").append(separator);
		sB.append("verboProcessar");
		
		return sB.toString();
	}

	public static String getInsertColumns(String separator)
	{
		if (separator.isEmpty()) {
			separator = SEPARATOR;
		}

		StringBuilder sB = new StringBuilder();
		sB.append("`id`").append(separator);
		sB.append("`title`").append(separator);
		sB.append("`description`").append(separator);
		sB.append("`complainOrigin`").append(separator);
		sB.append("`userState`").append(separator);
		sB.append("`userCity`").append(separator);
		sB.append("`status`").append(separator);
		sB.append("`created`").append(separator);
		sB.append("`modified`").append(separator);
		sB.append("`additionalFields`").append(separator);
		sB.append("`moderationUserName`").append(separator);
		sB.append("`otherProblemType`").append(separator);
		sB.append("`solved`").append(separator);
		sB.append("`dealAgain`").append(separator);
		sB.append("`otherProductType`").append(separator);
		sB.append("`type`").append(separator);
		sB.append("`evaluation`").append(separator);
		sB.append("`score`").append(separator);
		sB.append("`userRequestedDelete`").append(separator);
		sB.append("`requesterName`").append(separator);
		sB.append("`additionalInfo`").append(separator);
		sB.append("`evaluated`").append(separator);
		sB.append("`publishedEmailSent`").append(separator);
		sB.append("`deletionReason`").append(separator);
		sB.append("`read`").append(separator);
		sB.append("`marketplaceComplain`").append(separator);
		sB.append("`ip`").append(separator);
		sB.append("`moderationReasonDescription`").append(separator);
		sB.append("`frozen`").append(separator);
		sB.append("`requestEvaluation`").append(separator);
		sB.append("`moderateReason`").append(separator);
		sB.append("`firstInteractionDate`").append(separator);
		sB.append("`hasReply`").append(separator);
		sB.append("`compliment`").append(separator);
		sB.append("`legacyId`").append(separator);
		sB.append("`deletedIp`").append(separator);
		sB.append("`indexable`").append(separator);
		sB.append("`hasBacen`").append(separator);
		sB.append("`hasProcess`").append(separator);
		sB.append("`hasScore`").append(separator);
		sB.append("`hasFirstInteraction`").append(separator);
		sB.append("`timeToFirstInteractionGreaterThan0`").append(separator);
		sB.append("`timeToFirstInteractionGreaterThan1`").append(separator);
		sB.append("`timeToFirstInteractionGreaterThan2`").append(separator);
		sB.append("`timeToFirstInteractionGreaterThan3`").append(separator);
		sB.append("`timeToFirstInteractionGreaterThan4`").append(separator);
		sB.append("`timeToFirstInteractionGreaterThan5`").append(separator);
		sB.append("`timeToFirstInteractionGreaterThan6`").append(separator);
		sB.append("`hasQuestion`").append(separator);
		sB.append("`complainPeriod`").append(separator);
		sB.append("`hasEditadoPeloReclameAqui`").append(separator);
		sB.append("`hasProcon`").append(separator);
		sB.append("`verboReclamar`").append(separator);
		sB.append("`verboProcessar`");

		return sB.toString();
	}
	
	public String getComplainJson(JSONObject jsonObject)
	{
		StringBuilder sB = new StringBuilder();

		return getValuesSplitedBySeparator(jsonObject, SEPARATOR);
	}

	private String getValuesSplitedBySeparator(JSONObject jsonObject, String separator) {
		StringBuilder sB = new StringBuilder();

		sB.append((String) jsonObject.get("id")).append(separator);
		sB.append((String) jsonObject.get("title")).append(separator);
		sB.append((String) jsonObject.get("description")).append(separator);
		sB.append((String) jsonObject.get("complainOrigin")).append(separator);
		sB.append((String) jsonObject.get("userState")).append(separator);
		sB.append((String) jsonObject.get("userCity")).append(separator);
		sB.append((String) jsonObject.get("status")).append(separator);
		sB.append((String) jsonObject.get("created")).append(separator);
		sB.append((String) jsonObject.get("modified")).append(separator);
		sB.append((String) jsonObject.get("additionalFields")).append(separator);
		sB.append((String) jsonObject.get("moderationUserName")).append(separator);
		sB.append((String) jsonObject.get("otherProblemType")).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("solved")) ).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("dealAgain")) ).append(separator);
		sB.append((String) jsonObject.get("otherProductType")).append(separator);
		sB.append((String) jsonObject.get("type")).append(separator);
		sB.append((String) jsonObject.get("evaluation")).append(separator);
		sB.append( extractValueAsString((Double) jsonObject.get("score")) ).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("userRequestedDelete")) ).append(separator);
		sB.append((String) jsonObject.get("requesterName")).append(separator);
		sB.append((String) jsonObject.get("additionalInfo")).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("evaluated")) ).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("publishedEmailSent")) ).append(separator);
		sB.append((String) jsonObject.get("deletionReason")).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("read")) ).append(separator);
		sB.append((String) jsonObject.get("marketplaceComplain")).append(separator);
		sB.append((String) jsonObject.get("ip")).append(separator);
		sB.append((String) jsonObject.get("moderationReasonDescription")).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("frozen")) ).append(separator);
		sB.append((String) jsonObject.get("requestEvaluation")).append(separator);
		sB.append((String) jsonObject.get("moderateReason")).append(separator);
		sB.append((String) jsonObject.get("firstInteractionDate")).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("hasReply")) ).append(separator);
		sB.append( extractValueAsString((Boolean) jsonObject.get("compliment")) ).append(separator);
		sB.append( extractValueAsString((Long) jsonObject.get("legacyId")) ).append(separator);
		sB.append((String) jsonObject.get("deletedIp")).append(separator);
		sB.append((String) jsonObject.get("indexable")).append(separator);
		sB.append(hasBacen((String) jsonObject.get("description"))).append(separator);
		sB.append(hasProcess((String) jsonObject.get("description"))).append(separator);
		sB.append( hasScore( (Double) jsonObject.get("score")) ).append(separator);
		sB.append( ""+(jsonObject.get("firstInteractionDate") != null) ).append(separator);
		sB.append( timeToFirstInteractionGreaterThan( 0, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created")) ).append(separator);
		sB.append( timeToFirstInteractionGreaterThan( 1, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created")) ).append(separator);
		sB.append( timeToFirstInteractionGreaterThan( 2, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created")) ).append(separator);
		sB.append( timeToFirstInteractionGreaterThan( 3, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created")) ).append(separator);
		sB.append( timeToFirstInteractionGreaterThan( 4, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created")) ).append(separator);
		sB.append( timeToFirstInteractionGreaterThan( 5, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created")) ).append(separator);
		sB.append( timeToFirstInteractionGreaterThan( 6, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created")) ).append(separator);
		sB.append( hasQuestion( (String) jsonObject.get("description")) ).append(separator);
		sB.append( complainPeriod( (String) jsonObject.get("created")) ).append(separator);
		sB.append( hasEditadoPeloReclameAqui( (String) jsonObject.get("description")) ).append(separator);
		sB.append( hasProcon( (String) jsonObject.get("description")) ).append(separator);
		sB.append( verboReclamar( (String) jsonObject.get("description")) ).append(separator);
		sB.append( verboProcessar( (String) jsonObject.get("description")) );

		return sB.toString();
	}


	private String complainPeriod(String created) 
	{
		String result = "-";
		
		
		LocalDateTime createdDateTime = LocalDateTime.parse(created);
		int hour = createdDateTime.getHour();
		
		if( hour >= 0 & hour < 6)
		{
			result = "MADRUGADA";
		}
		else if( hour >= 6 & hour < 12 ) 
		{
			result = "MANHA";
		}
		else if( hour >= 12 & hour < 18 ) 
		{
			result = "TARDE";
		}
		else if( hour >= 18 & hour <= 23 )
		{
			result = "NOITE";
		}
		
		return result;
	}


	private String timeToFirstInteractionGreaterThan(int periodInDays, String firstInteractionDate, String created) 
	{
		LocalDateTime initDateTime = LocalDateTime.parse(created);
		LocalDateTime endDateTime = firstInteractionDate == null ? LocalDateTime.now() : LocalDateTime.parse(firstInteractionDate);
		
		long days = initDateTime.until( endDateTime, ChronoUnit.DAYS);
		
		return "" + (days > periodInDays);
	}


	public String hasProcess(String description) 
	{
		boolean flagContains = false;
		
		if(description != null)
		{
			if( description.contains("process") )
			{
				flagContains = true;
			}
		}
		
		return ""+ flagContains;
	}


	public String hasBacen(String description) 
	{
		boolean flagContains = false;
		
		if( description != null )
		{ 
			if( description.contains("bacen") || description.contains("banco central") )	
			{
				flagContains = true;
			}
		}
		
		return ""+ flagContains;
	}
	
	
	public String hasScore(Double score) 
	{
		return "" + (score != null);
	}


	public String extractValueAsString(Boolean field)
	{
		String value = "";
		
		if(field!=null)
		{
			value = field.toString();
		}	
		
		return value;
	}
	
	public String extractValueAsString(Double field)
	{
		String value = "";
		
		if(field!=null)
		{
			value = field.toString();
		}	
		
		return value;
	}
	
	public String extractValueAsString(Long field)
	{
		String value = "";
		
		if(field!=null)
		{
			value = field.toString();
		}	
		
		return value;
	}

	public ArrayList<Complain> getOpenComplainWithoutPrediction() {
		String query = "SELECT * \n" +
				"FROM complain \n" +
				"LEFT JOIN predicted_score ON \n" +
				"complain.id = predicted_score.complain_id \n" +
				"where score is null \n" +
				"and status = 'PENDING' \n" +
				"and predicted_score.complain_id is null";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<Complain> complains = new ArrayList<>();

		try {

			ResultSet rs = statement.executeQuery(query);
			Complain complain;
			while (rs.next())
			{
				complain = new Complain();
				complain.id = rs.getString("id");
				complain.title = rs.getString("title");
				complain.description = rs.getString("description");
				complain.complainOrigin = rs.getString("complainOrigin");
				complain.userState = rs.getString("userState");
				complain.userCity = rs.getString("userCity");
				complain.status = rs.getString("status");
				complain.created = new DateTime(rs.getDate("created"));
				complain.modified = new DateTime(rs.getDate("modified"));
				complain.additionalFields = rs.getString("additionalFields");
				complain.moderationUserName = rs.getString("moderationUserName");
				complain.otherProblemType = rs.getString("otherProblemType");
				complain.solved = rs.getString("solved");
				complain.otherProductType = rs.getString("otherProductType");
				complain.dealAgain = rs.getInt("dealAgain");
				complain.type = rs.getString("type");
				complain.evaluation = rs.getString("evaluation");
				complain.score = rs.getDouble("score");
				complain.userRequestedDelete = rs.getInt("userRequestedDelete");
				complain.requesterName = rs.getString("requesterName");
				complain.additionalInfo = rs.getString("additionalInfo");
				complain.evaluated = rs.getInt("evaluated");
				complain.publishedEmailSent = rs.getInt("publishedEmailSent");
				complain.deletionReason = rs.getString("deletionReason");
				complain.read = rs.getInt("read");
				complain.marketplaceComplain = rs.getString("marketplaceComplain");
				complain.ip = rs.getString("ip");
				complain.moderationReasonDescription = rs.getString("moderationReasonDescription");
				complain.frozen = rs.getInt("frozen");
				complain.requestEvaluation = new DateTime(rs.getDate("requestEvaluation"));
				complain.moderateReason = rs.getString("moderateReason");
				complain.firstInteractionDate = new DateTime(rs.getDate("firstInteractionDate"));
				complain.hasReply = rs.getInt("hasReply");
				complain.compliment = rs.getInt("compliment");
				complain.legacyId = rs.getString("legacyId");
				complain.deletedIp = rs.getString("deletedIp");
				complain.indexable = rs.getInt("indexable");
				complain.hasBacen = rs.getInt("hasBacen");
				complain.hasProcess = rs.getInt("hasProcess");
				complain.hasScore = rs.getInt("hasScore");
				complain.hasFirstInteraction = rs.getInt("hasFirstInteraction");
				complain.timeToFirstInteractionGreaterThan0 = rs.getInt("timeToFirstInteractionGreaterThan0");
				complain.timeToFirstInteractionGreaterThan1 = rs.getInt("timeToFirstInteractionGreaterThan1");
				complain.timeToFirstInteractionGreaterThan2 = rs.getInt("timeToFirstInteractionGreaterThan2");
				complain.timeToFirstInteractionGreaterThan3 = rs.getInt("timeToFirstInteractionGreaterThan3");
				complain.timeToFirstInteractionGreaterThan4 = rs.getInt("timeToFirstInteractionGreaterThan4");
				complain.timeToFirstInteractionGreaterThan5 = rs.getInt("timeToFirstInteractionGreaterThan5");
				complain.timeToFirstInteractionGreaterThan6 = rs.getInt("timeToFirstInteractionGreaterThan6");
				complain.hasQuestion = rs.getInt("hasQuestion");
				complain.complainPeriod = rs.getString("complainPeriod");
				complain.hasEditadoPeloReclameAqui = rs.getInt("hasEditadoPeloReclameAqui");
				complain.hasProcon = rs.getInt("hasProcon");
				complain.verboReclamar = rs.getInt("verboReclamar");
				complain.verboProcessar = rs.getInt("verboProcessar");

				complains.add(complain);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return complains;

	}

	public void saveToDatabase(JSONObject jsonObject) {
		Statement statement = MariaDbConnection.getInstance().getStatement();
		try {
			String command = "INSERT INTO complain(" + getInsertColumns(",") + ") VALUES ( "+ getInsertValues(jsonObject) +") ON DUPLICATE KEY UPDATE description = '"+ jsonObject.get("description") + "', score = "+ jsonObject.get("score") + ";";
			statement.executeUpdate(command);
			System.out.println("Inserted complain");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public ArrayList<QuantityByMonthYear> getLastYearComplains() {
		String query = "SELECT COUNT(id), MONTH(complain.created), YEAR(complain.created) FROM complain group by YEAR(complain.created), MONTH(complain.created) ORDER BY YEAR(complain.created) DESC, MONTH(complain.created) DESC LIMIT 12;";
		Statement statement = MariaDbConnection.getInstance().getStatement();

		ArrayList<QuantityByMonthYear> quantities = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				QuantityByMonthYear quantityByMonthYear = new QuantityByMonthYear();
				quantityByMonthYear.quantity = rs.getInt("COUNT(id)");
				quantityByMonthYear.month = rs.getInt("MONTH(complain.created)");
				quantityByMonthYear.year = rs.getInt("YEAR(complain.created)");

				quantities.add(quantityByMonthYear);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return quantities;
	}

	public ArrayList<QuantityDateDiff> getComplainsQuantityDateDiff() {
		String query = "SELECT COUNT(id), DATEDIFF(firstInteractionDate, created) FROM complain where created >= DATE_SUB(NOW(),INTERVAL 1 YEAR) AND DATEDIFF(firstInteractionDate, created) is not null group by DATEDIFF(firstInteractionDate, created) LIMIT 11";
		Statement statement = MariaDbConnection.getInstance().getStatement();

		ArrayList<QuantityDateDiff> quantities = new ArrayList<>();

		try {
			ResultSet rs = statement.executeQuery(query);
			while (rs.next())
			{
				QuantityDateDiff dateDiff = new QuantityDateDiff();
				dateDiff.quantity = rs.getInt("COUNT(id)");
				dateDiff.dateDiff = rs.getInt("DATEDIFF(firstInteractionDate, created)");

				quantities.add(dateDiff);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return quantities;
	}

	public Integer getComplainsWithoutEvaluation() {
		String query = "SELECT COUNT(id) FROM complain WHERE score is null";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		Integer complainsCount = 0;

		try {

			ResultSet rs = statement.executeQuery(query);

			while (rs.next())
			{
				complainsCount = rs.getInt("COUNT(id)");
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return complainsCount;
	}

	private String getInsertValues(JSONObject jsonObject) {
		StringBuilder sB = new StringBuilder();

		sB.append(getInsertStringValue(jsonObject.get("id")));
		sB.append(getInsertStringValue(jsonObject.get("title")));
		sB.append(getInsertStringValue(jsonObject.get("description")));
		sB.append(getInsertStringValue(jsonObject.get("complainOrigin")));
		sB.append(getInsertStringValue(jsonObject.get("userState")));
		sB.append(getInsertStringValue(jsonObject.get("userCity")));
		sB.append(getInsertStringValue(jsonObject.get("status")));
		sB.append(getInsertStringValue(jsonObject.get("created")));
		sB.append(getInsertStringValue(jsonObject.get("modified")));
		sB.append(getInsertStringValue(jsonObject.get("additionalFields")));
		sB.append(getInsertStringValue(jsonObject.get("moderationUserName")));
		sB.append(getInsertStringValue(jsonObject.get("otherProblemType")));
		sB.append(getInsertStringValue(jsonObject.get("solved")));
		sB.append(jsonObject.get("dealAgain")).append(",");
		sB.append(getInsertStringValue(jsonObject.get("otherProductType")));
		sB.append(getInsertStringValue(jsonObject.get("type")));
		sB.append(getInsertStringValue(jsonObject.get("evaluation")));
		sB.append(jsonObject.get("score")).append(",");
		sB.append(jsonObject.get("userRequestedDelete")).append(",");
		sB.append(getInsertStringValue(jsonObject.get("requesterName")));
		sB.append(getInsertStringValue(jsonObject.get("additionalInfo")));
		sB.append(jsonObject.get("evaluated")).append(",");
		sB.append(jsonObject.get("publishedEmailSent")).append(",");
		sB.append(getInsertStringValue(jsonObject.get("deletionReason")));
		sB.append(jsonObject.get("read")).append(",");
		sB.append(getInsertStringValue(jsonObject.get("ip")));
		sB.append(getInsertStringValue(jsonObject.get("marketplaceComplain")));
		sB.append(getInsertStringValue(jsonObject.get("moderationReasonDescription")));
		sB.append(jsonObject.get("frozen")).append(",");
		sB.append(getInsertStringValue(jsonObject.get("requestEvaluation")));
		sB.append(getInsertStringValue(jsonObject.get("moderateReason")));
		sB.append(getInsertStringValue(jsonObject.get("firstInteractionDate")));
		sB.append(jsonObject.get("hasReply")).append(",");
		sB.append(jsonObject.get("compliment")).append(",");
		sB.append(getInsertStringValue(jsonObject.get("legacyId")));
		sB.append(getInsertStringValue(jsonObject.get("deletedIp")));
		sB.append(jsonObject.get("indexable")).append(",");
		sB.append(hasBacen((String) jsonObject.get("description"))).append(",");
		sB.append(hasProcess((String) jsonObject.get("description"))).append(",");
		sB.append( hasScore( (Double) jsonObject.get("score"))).append(",");
		sB.append(jsonObject.get("firstInteractionDate") != null).append(",");
		sB.append(timeToFirstInteractionGreaterThan( 0, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created"))).append(",");
		sB.append(timeToFirstInteractionGreaterThan( 1, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created"))).append(",");
		sB.append(timeToFirstInteractionGreaterThan( 2, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created"))).append(",");
		sB.append(timeToFirstInteractionGreaterThan( 3, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created"))).append(",");
		sB.append(timeToFirstInteractionGreaterThan( 4, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created"))).append(",");
		sB.append(timeToFirstInteractionGreaterThan( 5, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created"))).append(",");
		sB.append(timeToFirstInteractionGreaterThan( 6, (String) jsonObject.get("firstInteractionDate"), (String) jsonObject.get("created"))).append(",");
		sB.append(hasQuestion( (String) jsonObject.get("description"))).append(",");
		sB.append("'").append(complainPeriod( (String) jsonObject.get("created"))).append("'").append(",");
		sB.append(hasEditadoPeloReclameAqui( (String) jsonObject.get("description"))).append(",");
		sB.append(hasProcon( (String) jsonObject.get("description"))).append(",");
		sB.append(verboReclamar( (String) jsonObject.get("description"))).append(",");
		sB.append(verboProcessar( (String) jsonObject.get("description")));
		return sB.toString();
	}

	private String getInsertStringValue(Object valueToBeTreated) {
		StringBuilder sB = new StringBuilder();

		if (valueToBeTreated == null) {
			sB.append(valueToBeTreated).append(",");
		} else {
			sB.append("'").append(valueToBeTreated).append("'").append(",");
		}
		return sB.toString();
	}
	
	private String hasQuestion(String description) 
	{
		boolean flagContains = false;
		
		if( description != null && description.contains("?") )
		{
			flagContains = true;
		}
		
		return ""+ flagContains;
	}
	
	private String hasEditadoPeloReclameAqui(String description) 
	{
		boolean flagContains = false;
		
		if( description != null && description.contains("[Editado pelo Reclame Aqui]") )
		{
			flagContains = true;
		}
		
		return ""+ flagContains;
	}
	
	private String hasProcon(String description) 
	{
		boolean flagContains = false;
		
		if( description != null && description.toLowerCase().contains("procon") )
		{
			flagContains = true;
		}
		
		return ""+ flagContains;
	}
	
	private String verboReclamar(String description) 
	{
		return ""+CogrooAnalyzer.hasLemma(NaturalLanguageProcessingUtil.getSentenceList(description), "v", "reclamar");
	}
	
	private String verboProcessar(String description) 
	{
		return ""+CogrooAnalyzer.hasLemma(NaturalLanguageProcessingUtil.getSentenceList(description), "v", "processar");
	}

	public ArrayList<Complain> getAll(Integer championId) {
		String query = "SELECT * " +
				"FROM complain " +
				"INNER JOIN predicted_score ON complain.id = predicted_score.complain_id " +
				"WHERE prediction_model_id = " + championId + " " +
				"ORDER BY created " +
				"LIMIT 20";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<Complain> complains = new ArrayList<>();

		try {

			ResultSet rs = statement.executeQuery(query);
			Complain complain;
			while (rs.next())
			{
				complain = new Complain();
				complain.id = rs.getString("id");
				complain.title = rs.getString("title");
				complain.description = rs.getString("description");
				complain.complainOrigin = rs.getString("complainOrigin");
				complain.userState = rs.getString("userState");
				complain.userCity = rs.getString("userCity");
				complain.status = rs.getString("status");
//				complain.created = new DateTime(rs.getDate("created"));
//				complain.modified = new DateTime(rs.getDate("modified"));
				complain.additionalFields = rs.getString("additionalFields");
				complain.moderationUserName = rs.getString("moderationUserName");
				complain.otherProblemType = rs.getString("otherProblemType");
				complain.solved = rs.getString("solved");
				complain.otherProductType = rs.getString("otherProductType");
				complain.dealAgain = rs.getInt("dealAgain");
				complain.type = rs.getString("type");
				complain.evaluation = rs.getString("evaluation");
				complain.score = rs.getDouble("score");
				complain.userRequestedDelete = rs.getInt("userRequestedDelete");
				complain.requesterName = rs.getString("requesterName");
				complain.additionalInfo = rs.getString("additionalInfo");
				complain.evaluated = rs.getInt("evaluated");
				complain.publishedEmailSent = rs.getInt("publishedEmailSent");
				complain.deletionReason = rs.getString("deletionReason");
				complain.read = rs.getInt("read");
				complain.marketplaceComplain = rs.getString("marketplaceComplain");
				complain.ip = rs.getString("ip");
				complain.moderationReasonDescription = rs.getString("moderationReasonDescription");
				complain.frozen = rs.getInt("frozen");
//				complain.requestEvaluation = new DateTime(rs.getDate("requestEvaluation"));
				complain.moderateReason = rs.getString("moderateReason");
//				complain.firstInteractionDate = new DateTime(rs.getDate("firstInteractionDate"));
				complain.hasReply = rs.getInt("hasReply");
				complain.compliment = rs.getInt("compliment");
				complain.legacyId = rs.getString("legacyId");
				complain.deletedIp = rs.getString("deletedIp");
				complain.indexable = rs.getInt("indexable");
				complain.hasBacen = rs.getInt("hasBacen");
				complain.hasProcess = rs.getInt("hasProcess");
				complain.hasScore = rs.getInt("hasScore");
				complain.hasFirstInteraction = rs.getInt("hasFirstInteraction");
				complain.timeToFirstInteractionGreaterThan0 = rs.getInt("timeToFirstInteractionGreaterThan0");
				complain.timeToFirstInteractionGreaterThan1 = rs.getInt("timeToFirstInteractionGreaterThan1");
				complain.timeToFirstInteractionGreaterThan2 = rs.getInt("timeToFirstInteractionGreaterThan2");
				complain.timeToFirstInteractionGreaterThan3 = rs.getInt("timeToFirstInteractionGreaterThan3");
				complain.timeToFirstInteractionGreaterThan4 = rs.getInt("timeToFirstInteractionGreaterThan4");
				complain.timeToFirstInteractionGreaterThan5 = rs.getInt("timeToFirstInteractionGreaterThan5");
				complain.timeToFirstInteractionGreaterThan6 = rs.getInt("timeToFirstInteractionGreaterThan6");
				complain.hasQuestion = rs.getInt("hasQuestion");
				complain.complainPeriod = rs.getString("complainPeriod");
				complain.hasEditadoPeloReclameAqui = rs.getInt("hasEditadoPeloReclameAqui");
				complain.hasProcon = rs.getInt("hasProcon");
				complain.verboReclamar = rs.getInt("verboReclamar");
				complain.verboProcessar = rs.getInt("verboProcessar");

				complain.predictedScore = rs.getDouble("predicted_score");
				complains.add(complain);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return complains;

	}

	public ArrayList<QuantityByStateViewModel> getComplainsByState() {
		String query = "SELECT COUNT(id), userState FROM complain group by userState";
		Statement statement = MariaDbConnection.getInstance().getStatement();
		ArrayList<QuantityByStateViewModel> complains = new ArrayList<>();

		try {

			ResultSet rs = statement.executeQuery(query);
			QuantityByStateViewModel complain;
			while (rs.next())
			{
				complain = new QuantityByStateViewModel();
				complain.quantity = rs.getString("COUNT(id)");
				complain.state = rs.getString("userState");

				complains.add(complain);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		return complains;
	}
}