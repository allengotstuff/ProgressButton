# ProgressButton
(testing from remote repo)
## A round rect button that can transform as a progress bar when downloading.

![alt text](https://media.giphy.com/media/Nmjo0F1BvKpJm/giphy.gif)

# How to use it

### Custom attributes:
        <attr name="outerStrokeWidth" format="dimension" />
        <attr name="outerStrokeColor" format="color"/>
        <attr name="buttonText" format="string"/>
        <attr name="buttonTextColor" format="color"/>
        <attr name="progressTextColor" format="color"/>
        <attr name="buttonColor" format="color"/>
        <attr name="maxProgress" format="integer"/>
        
### Usage example:
        <com.example.progessbuttonlib.ProgressView
            android:id="@+id/ProgressView"
            android:layout_width="300dp"
            android:layout_height="40dp"
            app:buttonText="Download"
            app:buttonColor= "@color/gray"
            android:layout_margin="10dp" />
            
### Runtime update progress:

    public void updateProgress()
