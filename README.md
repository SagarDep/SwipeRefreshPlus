# SwipeRefreshPlush
## 效果

![自定义refreshview效果](https://github.com/guhongya/SwipeRefreshPlush/blob/master/app/demo_capture.gif)
![默认效果演示](https://github.com/guhongya/SwipeRefreshPlush/blob/master/app/screenshot.gif)
##  简介
与SwipeRereshLayout类似，不过添加了下拉加载更多的功能，下拉支持fling,不会有卡顿。

## 基本特性
* 下拉刷新，上拉刷新
* 滑动到底部自动加载更多
* 上拉刷新没有数据时，可显示自定义view
* 支持AbsListView或NestChild的子类，如RecycleView,ListView  
* 自持自定义上拉刷新(可参考app中extention包中的LoadMoreController实现)和下拉刷新时的view显示和动画控制。
  
## 使用
### 基本使用
gradle:

	dependencies {
	         compile 'me.guhy:SwipeRefreshPlus:1.0.9'
	}
  
设置监听  

	swipeRefreshPlush.setOnScrollListener(new SwipeRefreshPlush.OnScrollListener() {
            @Override
            public void onPullDownToRefresh() {
               ......
            }

            @Override
            public void onPullUpToRefresh() {
                  ......
               }
        }); 	
	  	   
设置数据加载完，没有更多是显示的view
````
swipeRefreshPlush.setNoMoreView(noMoreView,layoutParams);
````

### 自定义下拉刷新，上拉加载更多的显示动画	      
自定义下拉刷新和上拉加载更多是通过给SwipeRefreshPlush设置新的控制类来实现的，新的控制类负责提供刷新时的view和刷新动画的控制，以及刷新监听的回调。

#### 自定义下拉刷新	
 新建类并实现 IRefreshViewController 接口（可参考app extension包中 MRefreshViewController 类）	
````
swipeRefreshPlush.setRefreshViewController(mRefreshViewController);//设置自定义RefreshViewController
````
	
#### 自定义上拉加载更多 
新建类并实现 ILoadViewController 接口（可参考app extension 包中 LoadMoreController 类）
 
	swipeRefreshPlush.setLoadViewController(new LoadMoreController())//设置自定义loadMoreController

### 其他：

	swipeRefreshPlush.setRefreshColorResources(new int[]{R.color.colorPrimary});//设置refresh的color
	swipeRefreshPlush.setRefresh(false);//设置是否显示refresh
	swipeRefreshPlush.showNoMore(false);//设置是否显示nomoreView,设置为true是拖动到底部将不再显示加载更多
	swipeRefreshPlush.setLoadMore(false);//设置是否显示加载更多
	swipeRefreshPlush.setScrollMode(@SwipeRefreshMode int mode)//设置模式



## Licence
	    
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

   	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
