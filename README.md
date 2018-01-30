# Maanta
 An Android Ui development pattern and Set of utilities to make your development faster and organized. Built over ButterKnife.

## How to use ?

- Copy the ViewUtils folder to your project
- Split your UI into basic elemental parts.
- Define basic Java objects to bind to these viewholders.
- Design any view, lists, implement endlesssrolling etc 
- never worry about using a findViewById or organizing views and adapters in your project.

### Add these to your build.gradle
```
  apt 'com.jakewharton:butterknife-compiler:8.2.1'
  compile 'com.jakewharton:butterknife:8.5.1'
```

# Usage
```
@ViewTypeId(id = ViewTypes.TWEET_SIMPLE_VIEW_TYPE_1) /*0. Dont miss this if this is a item in a listview , define a new integer id to each of your views. */

@LayoutFile(layoutId = R.layout.tweet_item) /*1. attach your layout file to the object */
public class TweetViewHolder extends ViewHolder<Tweet>{ /*2. Tweet is your data object */

    @BindView(R.id.retweet_button) /*3. bind all id's in your layout to your class variables */
    public TextView retweetButton;

    @BindView(R.id.user_name)
    public TextView userName;

    @BindView(R.id.user_profile_pic) 
    public ImageView userProfilePic;



    /*4. Implement your constructor*/
     public TweetViewHolder(Context ctx, ViewGroup parent){

    /*5. initalize your UI elements if any,  and event listeners etc...  don't set any UI elements with data yet*/

          retweetButton.setOnClickListener(new View.OnClickListener(){
                // do something with the tweet object like open a popup dialog etc
		openShareDialog(this.tweet);
          });

     }


     /*6. override render , that takes in the data object */
     @Override
     public TweetViewHolder render(final Tweet tweet) {
        this.tweet = tweet; // save this variable to use in the listeners etc
        /*7. Play with data & ui */
        userName.setText(tweet.user.name);
        Picasso.with(ctx).load(tweet.user.picture_url).into(userProfilePic);

     }


     /*8. For list views type holders, you can get hold of the adapter object to access adapter functions*/
     @Override
     public void setAdapter(RecyclerAdapter adapter) {
         this.adapter = (TweetsListAdapter)adapter; /*you can use this adapter object to put common functions inside the adapter and access it.*/
     }

    /*all other viewholder methods public/private*/


}
```

# Using this tweetViewHolder to render a list.

* Define your Adapter like this, Maanta keeps it short and simple, no cluttering bindings etc.
```

/*1. All types of viewholder this adapter supports*/
@SupportedViewHolders(viewHolders = {TweetViewHolder.class, Tweet2ViewHolder.class, ImagesGridViewHolder.class}) 
public class TweetsListAdapter extends RecyclerAdapter<Tweet> {
    /*2.  data is a array field containing list items */

    public void TweetListAdapter(List<Tweet> initialTweets){
        data.addAll(initialTweets);
        notifyDataSetChanged();
    }
    /*3. override getItemViewType and return viewType ids based on the item type */
    @Override
    public int getItemViewType(int position) {
        if(data.size()<=position){
            return ViewTypes.EMPTY_VIEW;
        }
        Tweet t =  data.get(position);
        if(t.isV2){
            return ViewTypes.V2_TWEET_VIEW;
        }
        return ViewTypes.SIMPLE_TWEET_VIEW;
    }


    public void addItems(boolean clear, List<Tweet> tweets){
        if(clear) data.clear();
        data.addAll(tweets);
        notifyDataSetChanged();
    }

}
```

* Remember that all the UI rendering takes place inside the individual ViewHolders only

### Implementing endless or paginated scrolling
```
      myRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {/* your recycler view can be attached to linearLayoutManager, staggeredGridLayoutManager etc*/
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                      // loadMoreMessages();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
       }
```
### Show an empty indicator for your listViews

in your layout create a empty view in the layout and  the RecyclerAdapter object has few special methods on setting and showing empty views
    and keep calling ifEmptyShowView() whenever you load data into the adapter. Lookup more details into these methods
```
    public RecyclerAdapter setEmptyView(View emptyView);
```


## License

This project is licensed under the MIT License

## Acknowledgments

* adapted from work by Abhilash Inumella @samosalabs

