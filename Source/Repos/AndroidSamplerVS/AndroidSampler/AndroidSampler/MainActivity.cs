using System;

using Android.App;
using Android.Content;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Android.OS;
using Android.Media;

namespace AndroidSampler
{
    [Activity(Label = "AndroidSampler", MainLauncher = true, Icon = "@mipmap/icon")]
    public class MainActivity : Activity
    {
        MediaPlayer _player;

        protected override void OnCreate(Bundle bundle)
        {
            base.OnCreate(bundle);

            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.Main);

            _player = MediaPlayer.Create(this, Resource.Raw.test);

            var button = FindViewById<Button>(Resource.Id.playButton);

            button.Click += delegate
            {
                _player.Start();
            };
        }
    }
}

