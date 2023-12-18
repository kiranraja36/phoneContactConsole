import java.util.*;
import java.util.regex.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
class CallHistory{
	String to_number;
	String from_Talk_time;
	String to_Talk_time;
	String date;
	long duration_minutes;
	long duration_seconds;
	
	CallHistory(String to_number,String from_Talk_time,String to_Talk_time,String date,long minutes,long seconds){
		this.to_number = to_number;
		this.from_Talk_time = from_Talk_time;
		this.to_Talk_time = to_Talk_time;
		this.date = date;
		this.duration_minutes = minutes;
		this.duration_seconds = seconds;
	}
}

class PhoneFunctionality{
	public static void call(Contact ccontact,String num){
		if(Contact.blockedNumbers.contains(ccontact.Primary_phoneNumber)) System.out.println("can't call "+num+" is blocked");
		else{
		LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String BformattedDateTime = currentDateTime.format(formatter);
        String[] before_date_time = BformattedDateTime.split(" ");
		boolean cut =false;
		Scanner sc = new Scanner(System.in);
		while(!cut){
		System.out.println("Speaking to "+num);
		System.out.println("press 1 to cut the call");
		if(sc.nextInt()==1) cut = true;
		}
		currentDateTime = LocalDateTime.now();
        String AformattedDateTime = currentDateTime.format(formatter);
        String[] after_date_time = AformattedDateTime.split(" ");
		LocalDateTime beforeDateTimeParsed = LocalDateTime.parse(BformattedDateTime, formatter);
        LocalDateTime afterDateTimeParsed = LocalDateTime.parse(AformattedDateTime, formatter);
		Duration duration = Duration.between(beforeDateTimeParsed, afterDateTimeParsed);
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds() % 60;
		CallHistory ch = new CallHistory(num,before_date_time[1],after_date_time[1],before_date_time[0],minutes,seconds);
		if(!ccontact.call_History.isEmpty()&& ccontact.call_History.size()>=5){
			ccontact.call_History.poll();
			ccontact.call_History.add(ch);
		}
		else{
			ccontact.call_History.add(ch);
		}
		System.out.println("Duration : " + String.valueOf(minutes)+" mins "+String.valueOf(seconds)+" secs ");
	}
	}
}
class Contact{
	String firstname;
	String lastName;
	String Primary_phoneNumber;
	String Secondary_phoneNumber;
	String email;
	Queue<CallHistory> call_History = new LinkedList<>();
	static ArrayList<String> blockedNumbers= new ArrayList<>();
	
	Contact(String firstname, String lastName,String primary_phoneNumber,String email){
		this.firstname = firstname;
		this.lastName = lastName;
		this.Primary_phoneNumber = primary_phoneNumber;
		this.email = email;
	}
	
}
public class Phone{
	public static void print(){
		System.out.println("---------------------------------------------------------------");
	}
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		HashMap<String,Contact> contact_details = new HashMap<>();
		HashMap<String,String> contact_details_phone = new HashMap<>();
		Contact cont1 = new Contact("Kiran","Raja","9629197247","kiranraja.036@gmail.com");
		Contact cont2 = new Contact("Raja","Ps","9789121297"," ");
		contact_details_phone.put("9629197247", "KiranRaja");
		contact_details_phone.put("9789121297", "RajaPs");
		contact_details.put("KiranRaja",cont1);
		contact_details.put("RajaPs",cont2);
		PhoneFunctionality pf = new PhoneFunctionality();
		boolean loop = true;
		while(loop){
			print();
			System.out.println("a) call\nb) create contact\nc) search contact\nd) delete contact\ne) update contact\nf) block a number\ng) call history\nh) exit");
			print();
			char choice = sc.next().charAt(0);
			sc.nextLine();
			switch(choice){
				case 'a':{
					String phno="";
					while(true){
							print();
							System.out.print("enter the phone number to call : ");
							 phno = sc.nextLine();
							if(Pattern.compile("[0-9]{10}").matcher(phno).matches()){
								break;
							}
							else{
								System.out.println("please!!! enter valid phone number ");
							}
						}
					Contact ccontact=null;
					if(contact_details_phone.containsKey(phno)){
						ccontact =contact_details.get(contact_details_phone.get(phno));
						phno = contact_details_phone.get(phno);
					}
			
					pf.call(ccontact,phno);
					break;
				}
				case 'b':{
					print();
					System.out.println("1. new contact \n2. existing contact");
					int n = sc.nextInt();
					sc.nextLine();
					if(n==1){
						System.out.print("enter the first name : ");
						String fname = sc.nextLine();
						fname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
						System.out.print("enter the last name : ");
						String lname = sc.nextLine();
						lname =lname.substring(0, 1).toUpperCase() + lname.substring(1);
						String phno="";
						while(true){
							System.out.print("enter the phone number : ");
							 phno = sc.nextLine();
							if(Pattern.compile("[0-9]{10}").matcher(phno).matches()){
								break;
							}
							else{
								System.out.println("please!!! enter valid phone number ");
							}
						}
						String email ="";
						while(true){
							System.out.print("enter email id : ");
							 email = sc.nextLine();
							if(Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$ ").matcher(email).matches()){
								break;
							}
							else if(email.length()==0){
								email="not having email";
								break;
							}
							else{
								System.out.println("Please!!! enter valid email id ");
							}
						}
						Contact cont = new Contact(fname,lname,phno,email);
						System.out.println("***Contact created Successfully***");
						contact_details.put(fname+lname,cont);
						contact_details_phone.put(phno,fname+lname);
					
					}
					else if(n==2){
						System.out.println("Enter the contact name to add number");
						String name = sc.nextLine();
						Contact ccontact;
						if(contact_details.containsKey(name)){
							ccontact = contact_details.get(name);
							System.out.println("Enter the  number ");
							String phno = sc.nextLine();
							ccontact.Secondary_phoneNumber = phno;
							contact_details_phone.put(phno,ccontact.firstname+ccontact.lastName);
							System.out.println("***number added to "+ccontact.firstname+ccontact.lastName+" Successfully***");
						}
						else{
							System.out.println("there is no contact named "+ name );
						}
					}
					break;
				}
				case 'c':{
					System.out.println("Enter the contact name to search");
					String name = sc.nextLine();
					if(contact_details.containsKey(name)){
						Contact ccontact = contact_details.get(name);
						System.out.println("-------- "+ccontact.firstname+ccontact.lastName+" -------");
						System.out.println("-> "+ccontact.Primary_phoneNumber);
						System.out.println("-> "+ccontact.Secondary_phoneNumber);
						System.out.println("-> "+ccontact.email);
						
					}
					else{
						System.out.println("***no Contact Found***");
					}
					break;
				}
				case 'd':{
					String phno;
					while(true){
							System.out.print("enter the phone number : ");
							 phno = sc.nextLine();
							if(Pattern.compile("[0-9]{10}").matcher(phno).matches()){
								break;
							}
							else{
								System.out.println("please!!! enter valid phone number ");
							}
					}
					if(contact_details_phone.containsKey(phno)){
						String ccname = contact_details_phone.get(phno);
						Contact ccontact =  contact_details.get(ccname);
						System.out.println(phno==ccontact.Primary_phoneNumber);
						if(phno.equals(ccontact.Primary_phoneNumber)){
							System.out.println("Do you want to delete the contact ?");
							System.out.println("1.Yes \n2.No");
							int num = sc.nextInt();
							sc.nextLine();
							if(num==1){
								contact_details.remove(phno);
								System.out.println("***Contact Deleted Successfully***");
							}
							else System.out.println("***Contact not Deleted ***");
							
						}
						else if(phno.equals(ccontact.Secondary_phoneNumber)){
							System.out.println("Do you want to delete the number ?");
							System.out.println("1.Yes \n2.No");
							int num = sc.nextInt();
							sc.nextLine();
							if(num==1){
								ccontact.Secondary_phoneNumber=" ";
								System.out.println("***number Deleted Successfully***");
							}
							else System.out.println("***number not Deleted ***");
							
						}
					}
					else{
						System.out.println("no number Found");
					}
					break;
				}
				case 'e':{
					String phno;
					while(true){
							System.out.print("enter the phone number to edit : ");
							 phno = sc.nextLine();
							if(Pattern.compile("[0-9]{10}").matcher(phno).matches()){
								break;
							}
							else{
								System.out.println("please!!! enter valid phone number ");
							}
					}
					if(contact_details_phone.containsKey(phno)){
						String ccname = contact_details_phone.get(phno);
						Contact ccontact =  contact_details.get(ccname);
						System.out.println("1.Edit Number \n2.Edit Name");
						int n = sc.nextInt();
						sc.nextLine();
						if(n==1){
							while(true){
									System.out.print("enter the phone number to edit : ");
									phno = sc.nextLine();
									if(Pattern.compile("[0-9]{10}").matcher(phno).matches()){
									break;
								}
								else{
									System.out.println("please!!! enter valid phone number ");
								}
							}
							System.out.println("1.Primary phoneNumber \n2.Secondary phoneNumber");
							if(n==1) ccontact.Primary_phoneNumber = phno;
							else if(n==2) ccontact.Secondary_phoneNumber = phno;
						}
						else if(n==2){
							System.out.print("enter the first name : ");
							String fname = sc.nextLine();
							ccontact.firstname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
							System.out.print("enter the last name : ");
							String lname = sc.nextLine();
							ccontact.lastName =lname.substring(0, 1).toUpperCase() + lname.substring(1);
							
						}
						contact_details.put(ccontact.firstname+ccontact.lastName,ccontact);
						contact_details_phone.put(phno,ccontact.firstname+ccontact.lastName);
					}
					else{
						System.out.println("***no number Found***");
					}
					break;
				}
				case 'f':{
					String phno;
					while(true){
							System.out.print("enter the phone number to block : ");
							 phno = sc.nextLine();
							if(Pattern.compile("[0-9]{10}").matcher(phno).matches()){
								break;
							}
							else{
								System.out.println("please!!! enter valid phone number ");
							}
					}
					Contact.blockedNumbers.add(phno);
					System.out.println("Contact blocked Successfully");
					break;
				}	
				case 'g':{
					String phno;
					while(true){
							System.out.print("enter the phone number to see history : ");
							 phno = sc.nextLine();
							if(Pattern.compile("[0-9]{10}").matcher(phno).matches()){
								break;
							}
							else{
								System.out.println("please!!! enter valid phone number ");
							}
					}
					if(contact_details_phone.containsKey(phno)){
						Contact ccontact = contact_details.get(contact_details_phone.get(phno));
						System.out.printf("%-15s %-8s %-8s %-8s %-15s \n","To number","from","To","Date","Duration");

						for(CallHistory i : ccontact.call_History){
							System.out.printf("%-15s %-8s %-8s %-8s %-15s \n",i.to_number,i.from_Talk_time,i.to_Talk_time,i.date,String.valueOf(i.duration_minutes)+"mins"+String.valueOf(i.duration_seconds)+"secs");
							
						}
					}
					break;
				}				
				case 'h':{
					loop=false;
					break;
				}
			}
		}
	}
}