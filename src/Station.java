import java.util.ArrayDeque;
import java.util.Deque;

public class Station{
	Integer name;
	float Avarage;
	Deque<Float> Q =new ArrayDeque<Float>();
	float X[] = new float[30];
	int n, insert;
	float sum;
	
	public Station(int a) {
		name = a;
	}
	public void printnr() {
		System.out.println(name);
	} 
 
	public void Temp_queue_aanpassen(float new_Float) {
		Q.remove();
		Q.add(new_Float);
	}
	
	//deze rekent voortschrijdend gemiddelde op basis van de temperaturen in de queue.
	public float Avarage() {
		float som = 0;
		for(float waarde: Q) {
			som = som += waarde;
		}
		Avarage= som / Q.size();
		return Avarage;
	}
	
	//deze berekent voorschrijdend gemiddeld met de waarde die ingevuld is in de methode.
	public float Extrapolate(float val) {
		if(n < X.length) n++;
		sum -= X[insert];
		sum += val;
		X[insert]= val;
		insert = (insert+1)% X.length;
		return (float)sum/n;

	}
}