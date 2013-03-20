package quali;

public class ContackPair {
private Contact contact_A;
private Contact contact_B;
public Contact getContact_A() {
	return contact_A;
}
public void setContact_A(Contact contact_A) {
	this.contact_A = contact_A;
}
public Contact getContact_B() {
	return contact_B;
}
public void setContact_B(Contact contact_B) {
	this.contact_B = contact_B;
}
public ContackPair(Contact contact_A, Contact contact_B) {
	super();
	this.contact_A = contact_A;
	this.contact_B = contact_B;
}

}
