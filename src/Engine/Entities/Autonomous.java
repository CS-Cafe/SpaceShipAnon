package Engine.Entities;

public interface Autonomous {

    /**
     * A method to instantiate a new {@code Autonomous} Object
     * at a location somewhere in the northward vicinity of this
     * {@code Autonomous} Object.
     *
     * @return a new {@code Autonomous} entity
     */
    default Autonomous climb(){
        return this;
    }

    /**
     * A method that shapes the default behavior of this
     * {@code Autonomous} Object, instantiating a new
     * {@code Autonomous} Object at the desired location.
     *
     * @return a new {@code Autonomous} entity
     */
    default Autonomous autopilot(){
        return this;
    }

    /**
     * A method to instantiate a new {@code Autonomous} Object
     * at a location directly south of this {@code Autonomous} Object
     *
     * @return a new {@code Autonomous} entity
     */
    default Autonomous scroll(){
        return this;
    }

    /**
     * A method to instantiate a new {@code Autonomous} Object
     * at a location somewhere in the southward vicinity of this
     * {@code Autonomous} Object.
     *
     * @return a new {@code Autonomous} entity
     */
    default Autonomous dive() {
        return this;
    }

}
