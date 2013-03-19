package com.yan.android;

public abstract interface AsyncTaskCompleteListener<T>
{
  public abstract void lauchNewHttpTask();
  public abstract void onTaskComplete(T paramT);
}

