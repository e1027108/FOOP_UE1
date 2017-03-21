package artifacts.permanent;

import artifacts.Artifact;

public class SizeDecreaseArtifact extends Artifact {

	private int decrease;

	public SizeDecreaseArtifact(int decrease) {
		this.decrease = decrease;
	}

	public int getDecrease() {
		return decrease;
	}
}
