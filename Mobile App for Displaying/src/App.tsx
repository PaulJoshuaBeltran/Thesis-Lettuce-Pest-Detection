import 'react-native-gesture-handler';
import * as React from 'react';
import {
  DarkTheme,
  DefaultTheme,
  NavigationContainer,
} from '@react-navigation/native';
import {
  Badge,
  BadgeIcon,
  BadgeText,
  Box,
  Button,
  ButtonText,
  FlatList,
  GluestackUIProvider,
  Icon,
  Pressable,
  StatusBar,
  Text,
  VStack,
  useColorMode,
  useToken,
  Select,
  SelectTrigger,
  SelectInput,
  SelectIcon,
  SelectPortal,
  SelectBackdrop,
  SelectContent,
  SelectDragIndicatorWrapper,
  SelectDragIndicator,
  SelectItem,
  Image,
  HStack,
} from '@gluestack-ui/themed';
import {config} from '@gluestack-ui/config';
import {createStackNavigator} from '@react-navigation/stack';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import HomeScreen from './HomeScreen.tsx';
import {Provider} from 'react-redux';
import {store} from './reducers/store.ts';

import Ionicons from 'react-native-vector-icons/Ionicons';
import {Dimensions, Platform, StyleSheet} from 'react-native';
import {BlurView} from '@react-native-community/blur';
import ScrollViewContainer from './ScrollViewContainer.tsx';
import styles from './styles.ts';
import {LineChart} from 'react-native-chart-kit';
import {useEffect, useRef, useState} from 'react';

import find from 'lodash/find';
import filter from 'lodash/filter';
import map from 'lodash/map';
import RNPickerSelect from 'react-native-picker-select';
import storage from '@react-native-firebase/storage';
import Video, {VideoRef} from 'react-native-video';

import {firebase} from '@react-native-firebase/database';
import '@react-native-firebase/firestore';
import firestore from '@react-native-firebase/firestore';

const getHeaderProps = ({colorMode}) => {
  const isDarkMode = colorMode === 'dark';

  return {
    headerBackground: () => (
      <Box
        borderBottomWidth={1 / 3}
        style={[StyleSheet.absoluteFill, {overflow: 'hidden'}]}>
        <BlurView
          blurAmount={32}
          blurType={isDarkMode ? 'dark' : 'xlight'}
          overlayColor="#00000000"
          style={StyleSheet.absoluteFill}
        />
      </Box>
    ),
    headerBackImage: ({tintColor}) => (
      <Icon
        as={Ionicons}
        color={tintColor}
        name="arrow-back-outline"
        size="xl"
        style={styles.headerBackButton}
      />
    ),
    headerBackTitleVisible: false,
    headerMode: 'screen',
    headerTransparent: true,
  };
};

const getStackNavigatorProps = ({colorMode}) => {
  return {
    screenOptions: {
      ...getHeaderProps({colorMode}),
    },
  };
};

const HomeStack = createStackNavigator();

function FilesScreen(): React.JSX.Element {
  return (
    <ScrollViewContainer>
      <Box style={styles.sectionContainer}>
        <Text>Hello, World!</Text>
      </Box>
    </ScrollViewContainer>
  );
}

function HomeStackScreen(): React.JSX.Element {
  const colorMode = useColorMode();

  return (
    <HomeStack.Navigator {...getStackNavigatorProps({colorMode})}>
      <HomeStack.Screen component={HomeScreen} name="Home" />
      <HomeStack.Screen component={FilesScreen} name="Files" />
    </HomeStack.Navigator>
  );
}

const SensorsStack = createStackNavigator();

function SensorsScreen(): React.JSX.Element {
  const [pH, setPH] = useState(0.0);
  const [illuminance, setIlluminance] = useState(0.0);
  const [waterLevel1, setWaterLevel1] = useState(0.0);
  const [waterLevel2, setWaterLevel2] = useState(0.0);
  const [temperature, setTemperature] = useState(0.0);
  const [humidity, setHumidity] = useState(0.0);
  const [conductivity, setConductivity] = useState(0.0);

  const DATA = [
    {
      iconName: 'flask',
      iconNameOutline: 'flask-outline',
      id: 'pH',
      firebaseId: 'ph_sensor',
      isOnline: true,
      name: 'pH',
      value: pH,
      setter: setPH,
    },
    {
      baseUnitSymbol: 'lux',
      iconName: 'sunny',
      iconNameOutline: 'sunny-outline',
      id: 'illuminance',
      firebaseId: 'lux_sensor',
      isOnline: true,
      name: 'Illuminance',
      value: illuminance,
      setter: setIlluminance,
    },
    {
      baseUnitSymbol: 'cm',
      iconName: 'water',
      iconNameOutline: 'water-outline',
      id: 'waterLevel1',
      firebaseId: 'water_level_sensor1',
      isOnline: true,
      name: 'Water Level 1',
      value: waterLevel1,
      setter: setWaterLevel1,
    },
    {
      baseUnitSymbol: 'cm',
      iconName: 'water',
      iconNameOutline: 'water-outline',
      id: 'waterLevel2',
      firebaseId: 'water_level_sensor2',
      isOnline: true,
      name: 'Water Level 2',
      value: waterLevel2,
      setter: setWaterLevel2,
    },
    {
      baseUnitSymbol: '°C',
      iconName: 'thermometer',
      iconNameOutline: 'thermometer-outline',
      id: 'temperature',
      firebaseId: 'temperature_sensor',
      isOnline: true,
      name: 'Temperature',
      value: temperature,
      setter: setTemperature,
    },
    // {
    //   baseUnitSymbol: '°C',
    //   iconName: 'thermometer',
    //   iconNameOutline: 'thermometer-outline',
    //   id: 'temperature2',
    //   isOnline: true,
    //   name: 'Temperature 2',
    //   value: 31.3,
    // },
    {
      baseUnitSymbol: 'g m³',
      baseUnitSymbol: '%',
      iconName: 'water',
      iconNameOutline: 'water-outline',
      id: 'humidity',
      firebaseId: 'humidity_sensor',
      isOnline: true,
      name: 'Humidity',
      value: humidity,
      setter: setHumidity,
    },
    {
      // baseUnitSymbol: 'S/m',
      baseUnitSymbol: 'ppm',
      iconName: 'flash',
      iconNameOutline: 'flash-outline',
      id: 'conductivity',
      firebaseId: 'TDS_sensor',
      isOnline: true,
      name: 'Conductivity',
      value: conductivity,
      setter: setConductivity,
    },
  ];

  const [selectedId, setSelectedId] = useState<string>();
  const [baseUnitSymbol, setBaseUnitSymbol] = useState();

  const resolvedSize = useToken('space', '6');

  const [sensors, setSensors] = useState();

  const reference = firebase
    .app()
    .database(
      'https://my-first-project-4b80b-default-rtdb.asia-southeast1.firebasedatabase.app/',
    )
    .ref('/sensors');

  const renderItem = ({item, index}) => {
    // @ts-ignore
    return (
      <Pressable
        onPress={() => {
          setSelectedId(item.id);
          setBaseUnitSymbol(find(DATA, {id: item.id}).baseUnitSymbol);
        }}>
        {/*@ts-ignore*/}
        {({pressed}) => (
          <Box
            borderRadius="$xl"
            borderWidth="$1"
            marginLeft="$2"
            maxWidth="$40"
            minWidth="$40"
            padding="$4"
            transform={[{scale: pressed ? 0.96 : 1}]}
            {...(index === 0
              ? {marginLeft: styles.sectionContainer.paddingHorizontal}
              : index === DATA.length - 1
              ? {marginRight: styles.sectionContainer.paddingHorizontal}
              : null)}>
            <VStack space="xs">
              <Box>
                <Icon
                  as={Ionicons}
                  color="$primary500"
                  marginBottom="$2"
                  name={
                    pressed || selectedId === item.id
                      ? item.iconName
                      : item.iconNameOutline
                  }
                  size={resolvedSize}
                />
                {/*{sensors ? (*/}
                {/*  <Text bold color="$black">*/}
                {/*    {sensors*/}
                {/*      .filter(element => element.sensorId === item.id)*/}
                {/*      .map(element => parseFloat(element.value))*/}
                {/*      .slice(-1)}*/}
                {/*    /!*{item.value.toFixed(2)}*!/*/}
                {/*    {item.baseUnitSymbol*/}
                {/*      ? item.baseUnitSymbol === '%'*/}
                {/*        ? null*/}
                {/*        : ' '*/}
                {/*      : null}*/}
                {/*    {item.baseUnitSymbol}*/}
                {/*  </Text>*/}
                {/*) : null}*/}
                {item.value ? (
                  <Text bold color="$black">
                    {item.value.toFixed(2)}
                    {item.baseUnitSymbol
                      ? item.baseUnitSymbol === '%'
                        ? null
                        : ' '
                      : null}
                    {item.baseUnitSymbol}
                  </Text>
                ) : null}
                <Text bold>{item.name}</Text>
              </Box>
              {/*<Badge*/}
              {/*  action={item.isOnline ? 'success' : 'error'}*/}
              {/*  alignSelf="start">*/}
              {/*  <BadgeText>{item.isOnline ? 'Online' : 'Offline'}</BadgeText>*/}
              {/*  <BadgeIcon*/}
              {/*    as={Ionicons}*/}
              {/*    marginLeft="$2"*/}
              {/*    name={item.isOnline ? 'happy-outline' : 'sad-outline'}*/}
              {/*  />*/}
              {/*</Badge>*/}
            </VStack>
          </Box>
        )}
      </Pressable>
    );
  };

  const [width, setWidth] = useState();

  const handleLayout = ({nativeEvent}) => {
    setWidth(nativeEvent.layout.width);
  };

  const resolvedColor = useToken('colors', 'primary500');

  // Your secondary Firebase project credentials for Android...
  const androidCredentials = {
    clientId: '276257796909-sr4lhhmt45vh8gjla9rv73ea0ld0dh5l.apps.googleusercontent.com',
    appId: '1:276257796909:android:f1194b0f629f6eb2a2d9a4',
    apiKey: 'AIzaSyCvoea5UgF7qco-xTJGwaL0YpoSB1W1xE0',
    databaseURL: 'https://thesis-hydroponics-v5-default-rtdb.asia-southeast1.firebasedatabase.app',
    storageBucket: 'thesis-hydroponics-v5.appspot.com',
    messagingSenderId: '276257796909',
    projectId: 'thesis-hydroponics-v5',
  };

// Your secondary Firebase project credentials for iOS...
  const iosCredentials = {
    clientId: '276257796909-qf0vt8toafrdl23lona45tgs3d9cfrom.apps.googleusercontent.com',
    appId: '1:276257796909:ios:32ef6994065c2843a2d9a4',
    apiKey: 'AIzaSyDaeIhKjj2xtl7iPxkJ0JY-Bso8MAN9jgo',
    databaseURL: 'https://thesis-hydroponics-v5-default-rtdb.asia-southeast1.firebasedatabase.app',
    storageBucket: 'thesis-hydroponics-v5.appspot.com',
    messagingSenderId: '276257796909',
    projectId: 'thesis-hydroponics-v5',
  };

// Select the relevant credentials
  const credentials = Platform.select({
    android: androidCredentials,
    ios: iosCredentials,
  });

  const config = {
    name: 'SECONDARY_APP',
  };

  async function baz() {
    try {
      const apps = firebase.apps;

      try {
        await firebase.initializeApp(credentials, config);
      } catch (error) {
        console.log("error: ", error);
      }
      apps.forEach(app => {
        console.log('App name: ', app.name);
      });

      await firebase.app('SECONDARY_APP');

      try {
        for (let i = 0; i < DATA.length; i++) {
          console.log('DATA[i].firebaseId: ', DATA[i].firebaseId);
          await firebase.app('SECONDARY_APP').firestore()
            .collection('Sensors_Infos')
            .doc(DATA[i].firebaseId)
            .get()
            .then(documentSnapshot => {
              console.log('Data exists: ', documentSnapshot.exists);

              if (documentSnapshot.exists) {
                const data = documentSnapshot.data();
                const keys = Object.keys(data).sort((a, b) => {
                  const parseDateString = (dateString) => {
                    const [datePart, timePart] = dateString.split(' '); // Split date and time parts
                    const [month, day, year] = datePart.split('/'); // Split the date into month, day, and year
                    const [hour, minute, second] = timePart.split(':'); // Split the time into hour, minute, and second
                    return new Date(year, month - 1, day, hour, minute, second); // Note: month - 1 because months are 0-indexed in JavaScript Date objects
                  };

                  const dateA = parseDateString(a);
                  const dateB = parseDateString(b);
                  // console.log('dateA:', isoDateA, 'dateB:', isoDateB); // Add this line for debugging
                  // console.log('dateA:', dateA, 'dateB:', dateB); // Add this line for debugging
                  return dateB - dateA;
                });
                console.log(keys);
                const latestKey = keys[0];
                const newValue = data[latestKey];
                DATA[i].setter(newValue);

              }
            });
        }
      } catch (error) {
        console.log('error!: ', error);
      }

      // console.log(await firebase.app('SECONDARY_APP').storage().ref('/public/128_36cm.jpg').getDownloadURL());
      await firebase.app('[DEFAULT]');
    } catch (error) {
      console.log('error: ', error);
    }
  }

  useEffect(() => {
    reference.once('value').then(snapshot => {
      console.log('User data: ', snapshot.val());
      setSensors(snapshot.val());
    });

    baz();
  }, []);

  return (
    <ScrollViewContainer>
      <Box style={styles.sectionContainer} />
      <FlatList
        data={DATA}
        horizontal
        renderItem={({item, index}) => renderItem({item, index})}
        showsHorizontalScrollIndicator={false}
      />
      <Box style={styles.sectionContainer}>
        <VStack space="xs">
          <Button onPress={async () => {
            try {
              const apps = firebase.apps;

              // await firebase.initializeApp(credentials, config);
              apps.forEach(app => {
                console.log('App name: ', app.name);
              });

              await firebase.app('SECONDARY_APP')

              // try {
              //   await firebase.app('SECONDARY_APP').firestore()
              //     .collection('Sensors_Infos')
              //     .doc('request_avail')
              //     .update({
              //       request_avail: 1,
              //     })
              //     .then(() => {
              //       console.log('request_avail updated!');
              //     });
              // } catch (error) {
              //   console.log('error!: ', error);
              // }

              try {
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Sensors_Infos')
                  .doc('request_sensor_val')
                  .update({
                    request_val: 1,
                  })
                  .then(() => {
                    console.log('request_sensor_val updated!');
                  });
              } catch (error) {
                console.log('error!: ', error);
              }

              // console.log(await firebase.app('SECONDARY_APP').storage().ref('/public/128_36cm.jpg').getDownloadURL());
              await firebase.app('[DEFAULT]');
            } catch (error) {
              console.log('error: ', error);
            }
          }}>
            <ButtonText>Record sensor values</ButtonText>
          </Button>
          <Button onPress={async () => {
            try {
              const apps = firebase.apps;

              // await firebase.initializeApp(credentials, config);
              apps.forEach(app => {
                console.log('App name: ', app.name);
              });

              await firebase.app('SECONDARY_APP')

              try {
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Sensors_Infos')
                  .doc('request_avail')
                  .update({
                    request_avail: 1,
                  })
                  .then(() => {
                    console.log('request_avail updated!');
                  });
              } catch (error) {
                console.log('error!: ', error);
              }

              try {
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Sensors_Infos')
                  .doc('request_sensor_val')
                  .update({
                    request_val: 1,
                  })
                  .then(() => {
                    console.log('request_avail updated!');
                  });
              } catch (error) {
                console.log('error!: ', error);
              }

              try {
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Cameras_Infos')
                  .doc('Android_cam')
                  .update({
                    request_avail: 1,
                    request_img: 1,
                  })
                  .then(() => {
                    console.log('request_sensor_val updated!');
                  });
              } catch (error) {
                console.log('error!: ', error);
              }

              // console.log(await firebase.app('SECONDARY_APP').storage().ref('/public/128_36cm.jpg').getDownloadURL());
              await firebase.app('[DEFAULT]');
            } catch (error) {
              console.log('error: ', error);
            }
          }}>
            <ButtonText>Take a photo</ButtonText>
          </Button>
          <Button onPress={async () => {
            try {
              const apps = firebase.apps;

              // await firebase.initializeApp(credentials, config);
              apps.forEach(app => {
                console.log('App name: ', app.name);
              });

              await firebase.app('SECONDARY_APP')

              try {
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Sensors_Infos')
                  .doc('request_avail')
                  .update({
                    request_avail: 1,
                  })
                  .then(() => {
                    console.log('request_avail updated!');
                  });
              } catch (error) {
                console.log('error!: ', error);
              }

              try {
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Camera_Infos')
                  .doc('is_avail')
                  .update({
                    // is_avail: 1,
                  })
                  .then(() => {
                    console.log('request_sensor_val updated!');
                  });
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Camera_Infos')
                  .doc('request_avail')
                  .update({
                    // request_avail: 1,
                  })
                  .then(() => {
                    console.log('request_sensor_val updated!');
                  });
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Camera_Infos')
                  .doc('request_cam_vid')
                  .update({
                    request_cam_vid: 1,
                  })
                  .then(() => {
                    console.log('request_sensor_val updated!');
                  });
              } catch (error) {
                console.log('error!: ', error);
              }

              // console.log(await firebase.app('SECONDARY_APP').storage().ref('/public/128_36cm.jpg').getDownloadURL());
              await firebase.app('[DEFAULT]');
            } catch (error) {
              console.log('error: ', error);
            }
          }}>
            <ButtonText>Record a video</ButtonText>
          </Button>
        </VStack>
      </Box>
      {/*{selectedId ? (*/}
      {/*  <Box style={styles.sectionContainer}>*/}
      {/*    <Box onLayout={({nativeEvent}) => handleLayout({nativeEvent})}>*/}
      {/*      {width && sensors ? (*/}
      {/*        // @ts-ignore*/}
      {/*        <LineChart*/}
      {/*          data={{*/}
      {/*            // labels: [*/}
      {/*            //   '2024-01-01',*/}
      {/*            //   '2024-01-02',*/}
      {/*            //   '2024-01-04',*/}
      {/*            //   '2024-01-06',*/}
      {/*            //   '2024-01-08',*/}
      {/*            //   '2024-01-11',*/}
      {/*            // ].map(element => {*/}
      {/*            //   const date = new Date(element);*/}
      {/*            //*/}
      {/*            //   return `${date.getMonth() + 1}/${date.getDate()}`;*/}
      {/*            // }),*/}
      {/*            labels: Array.from(*/}
      {/*              {*/}
      {/*                length: sensors*/}
      {/*                  .filter(element => element.sensorId === selectedId)*/}
      {/*                  .map(element => parseFloat(element.value)).length,*/}
      {/*              },*/}
      {/*              (_, i) => `${i + 1}`,*/}
      {/*            ),*/}
      {/*            datasets: [*/}
      {/*              {*/}
      {/*                data: sensors*/}
      {/*                  .filter(element => element.sensorId === selectedId)*/}
      {/*                  .map(element => parseFloat(element.value)),*/}
      {/*              },*/}
      {/*            ],*/}
      {/*          }}*/}
      {/*          width={width}*/}
      {/*          height={220}*/}
      {/*          yAxisSuffix={*/}
      {/*            baseUnitSymbol*/}
      {/*              ? selectedId === 'humidity'*/}
      {/*                ? baseUnitSymbol*/}
      {/*                : ` ${baseUnitSymbol}`*/}
      {/*              : undefined*/}
      {/*          }*/}
      {/*          chartConfig={{*/}
      {/*            backgroundGradientFromOpacity: 0,*/}
      {/*            backgroundGradientToOpacity: 0,*/}
      {/*            color: () => resolvedColor,*/}
      {/*          }}*/}
      {/*        />*/}
      {/*      ) : null}*/}
      {/*    </Box>*/}
      {/*  </Box>*/}
      {/*) : null}*/}
    </ScrollViewContainer>
  );
}

function SensorsStackScreen(): React.JSX.Element {
  const colorMode = useColorMode();

  return (
    <SensorsStack.Navigator {...getStackNavigatorProps({colorMode})}>
      <SensorsStack.Screen component={SensorsScreen} name="Sensors" />
    </SensorsStack.Navigator>
  );
}

const LettuceGrowthStack = createStackNavigator();

function LettuceGrowthScreen(): React.JSX.Element {
  const resolvedSize = useToken('space', '6');

  let trialNumbers = [];

  for (let i = 1; i <= 14; i++) {
    trialNumbers.push(i.toString());
  }

  for (
    let charCode = 'A'.charCodeAt(0);
    charCode < 'H'.charCodeAt(0);
    charCode++
  ) {
    trialNumbers.push('15' + String.fromCharCode(charCode));
  }

  for (let i = 16; i <= 25; i++) {
    if (i != 22) {
      trialNumbers.push(i.toString());
    }
  }

  const [selectedTrialNumber1, setSelectedTrialNumber1] = useState();
  const [selectedTrialNumber2, setSelectedTrialNumber2] = useState();

  const pickerSelectStyles = StyleSheet.create({
    inputIOS: {
      fontSize: 16,
      paddingVertical: 12,
      paddingHorizontal: 10,
      borderWidth: 1,
      borderColor: 'gray',
      borderRadius: 4,
      color: 'black',
      paddingRight: 30, // to ensure the text is never behind the icon
    },
    inputAndroid: {
      fontSize: 16,
      paddingHorizontal: 10,
      paddingVertical: 8,
      borderWidth: 0.5,
      // borderColor: 'purple',
      borderColor: 'gray',
      borderRadius: 8,
      color: 'black',
      paddingRight: 30, // to ensure the text is never behind the icon
    },
  });

  const [width, setWidth] = useState();

  const handleLayout = ({nativeEvent}) => {
    setWidth(nativeEvent.layout.width);
  };

  const resolvedPrimary = useToken('colors', 'primary500');
  const resolvedSecondary = useToken('colors', 'secondary500');

  const [selectedDayNumber, setSelectedDayNumber] = useState();

  function getDownloadURLs(reference, pageToken) {
    let downloadURLs = [];

    function listFilesAndDirectories(reference, pageToken) {
      return reference.list({pageToken}).then(async result => {
        // // Loop over each item
        // result.items.forEach(ref => {
        //   console.log(ref.fullPath);
        // });

        const promise = await Promise.all(
          result.items.map(ref => storage().ref(ref.fullPath).getDownloadURL()),
        );

        downloadURLs.push(...promise);

        if (result.nextPageToken) {
          return listFilesAndDirectories(reference, result.nextPageToken);
        }

        console.log(`downloadURLs = ${JSON.stringify(downloadURLs)}`);

        return Promise.resolve(downloadURLs);
      });
    }

    return listFilesAndDirectories(reference, pageToken);
  }

  const reference = storage().ref('images');

  const reference1 = firebase
    .app()
    .database(
      'https://my-first-project-4b80b-default-rtdb.asia-southeast1.firebasedatabase.app/',
    )
    .ref('/lettuce_growth');

  const [downloadURLs, setDownloadURLs] = useState();
  const [lettuceGrowth, setLettuceGrowth] = useState();
  const [lettuceHeight, setLettuceHeight] = useState();

  function foo(
    lettuceGrowth,
    selectedTrialNumber1,
    selectedTrialNumber2,
    selectedDayNumber,
  ) {
    const heights1 = lettuceGrowth
      .filter(item => item.trial_number === selectedTrialNumber1)
      .map(item => parseFloat(item.lettuce_height));
    const heights2 = lettuceGrowth
      .filter(item => item.trial_number === selectedTrialNumber2)
      .map(item => parseFloat(item.lettuce_height));

    const height1 =
      heights1.length > selectedDayNumber - 1
        ? heights1[selectedDayNumber - 1]
        : 0;
    const height2 =
      heights2.length > selectedDayNumber - 1
        ? heights2[selectedDayNumber - 1]
        : 0;

    console.log('height1: ', height1);
    console.log('height2: ', height2);

    let percentageDifference;

    if (height1 !== 0 && height2 !== 0) {
      const averageHeight = (height1 + height2) / 2;
      // Avoid division by zero by ensuring the average height is non-zero
      if (averageHeight !== 0) {
        percentageDifference =
          (Math.abs(height2 - height1) / averageHeight) * 100;
        percentageDifference = `${percentageDifference.toFixed(2)}%`;
      } else {
        console.log('No valid average height for comparison.');
        percentageDifference = 'N/A';
      }
    } else {
      console.log('No growth recorded for both trials on this day.');
      percentageDifference = 'N/A';
    }

    return percentageDifference;
  }

  function bar(
    lettuceGrowth,
    selectedTrialNumber1,
    selectedTrialNumber2,
    selectedDayNumber,
  ) {
    const heights1 = lettuceGrowth
      .filter(item => item.trial_number === selectedTrialNumber1)
      .map(item => parseFloat(item.lettuce_height));
    const heights2 = lettuceGrowth
      .filter(item => item.trial_number === selectedTrialNumber2)
      .map(item => parseFloat(item.lettuce_height));

    // Calculate the minimum length to ensure comparison on the same basis
    const minLength = Math.min(heights1.length, heights2.length);

    let totalPercentageDifference = 0;
    let daysCompared = 0;

    for (let day = 0; day < minLength; day++) {
      if (heights1[day] !== 0 || heights2[day] !== 0) {
        const averageHeight = (heights1[day] + heights2[day]) / 2;
        if (averageHeight !== 0) {
          // Ensure no division by zero
          const percentageDifference =
            (Math.abs(heights2[day] - heights1[day]) / averageHeight) * 100;
          totalPercentageDifference += percentageDifference;
          daysCompared++;
        }
      }
    }

    // Calculate average percentage difference
    const averagePercentageDifference =
      daysCompared > 0 ? totalPercentageDifference / daysCompared : 0;

    return `${averagePercentageDifference.toFixed(2)}%`;
  }

  // Your secondary Firebase project credentials for Android...
  const androidCredentials = {
    clientId: '276257796909-sr4lhhmt45vh8gjla9rv73ea0ld0dh5l.apps.googleusercontent.com',
    appId: '1:276257796909:android:f1194b0f629f6eb2a2d9a4',
    apiKey: 'AIzaSyCvoea5UgF7qco-xTJGwaL0YpoSB1W1xE0',
    databaseURL: 'https://thesis-hydroponics-v5-default-rtdb.asia-southeast1.firebasedatabase.app',
    storageBucket: 'thesis-hydroponics-v5.appspot.com',
    messagingSenderId: '276257796909',
    projectId: 'thesis-hydroponics-v5',
  };

// Your secondary Firebase project credentials for iOS...
  const iosCredentials = {
    clientId: '276257796909-qf0vt8toafrdl23lona45tgs3d9cfrom.apps.googleusercontent.com',
    appId: '1:276257796909:ios:32ef6994065c2843a2d9a4',
    apiKey: 'AIzaSyDaeIhKjj2xtl7iPxkJ0JY-Bso8MAN9jgo',
    databaseURL: 'https://thesis-hydroponics-v5-default-rtdb.asia-southeast1.firebasedatabase.app',
    storageBucket: 'thesis-hydroponics-v5.appspot.com',
    messagingSenderId: '276257796909',
    projectId: 'thesis-hydroponics-v5',
  };

// Select the relevant credentials
  const credentials = Platform.select({
    android: androidCredentials,
    ios: iosCredentials,
  });

  const config = {
    name: 'SECONDARY_APP',
  };

  async function baz() {
    try {
      await firebase.initializeApp(credentials, config);
    } catch (error) {
      console.log('ERRORRR: ', error);
    }
    await firebase.app('SECONDARY_APP');

    try {
      await firebase.app('SECONDARY_APP').firestore()
        .collection('Camera_Infos')
        .doc('Android_cam')
        .get()
        .then(documentSnapshot => {
          console.log('Data exists: ', documentSnapshot.exists);

          if (documentSnapshot.exists) {
            const data = documentSnapshot.data();

            setLettuceHeight(data['lettuce_height']);
          }
        });
    } catch (error) {
      console.log('error!: ', error);
    }

    await firebase.app('[DEFAULT]');
  }

  useEffect(() => {
    reference1.once('value').then(snapshot => {
      console.log('User data: ', snapshot.val());
      setLettuceGrowth(snapshot.val());
    });

    getDownloadURLs(reference).then(value => {
      console.log(`value: ${JSON.stringify(value)}`);
      setDownloadURLs(value);
    });

    baz();
  }, []);

  return (
    <ScrollViewContainer>
      <Box style={styles.sectionContainer}>
        <VStack space="xs">
          <RNPickerSelect
            Icon={() => (
              <Icon
                as={Ionicons}
                name="chevron-down-outline"
                size={resolvedSize}
              />
            )}
            items={trialNumbers.map(element => ({
              label: element,
              value: element,
            }))}
            onValueChange={value => setSelectedTrialNumber1(value)}
            placeholder={{label: 'Trial Number 1'}}
            style={{
              ...pickerSelectStyles,
              iconContainer: {
                top: 10,
                right: 12,
              },
            }}
            useNativeAndroidPickerStyle={false}
            value={selectedTrialNumber1}
          />
          <RNPickerSelect
            Icon={() => (
              <Icon
                as={Ionicons}
                name="chevron-down-outline"
                size={resolvedSize}
              />
            )}
            items={trialNumbers.map(element => ({
              label: element,
              value: element,
            }))}
            onValueChange={value => setSelectedTrialNumber2(value)}
            placeholder={{label: 'Trial Number 2'}}
            style={{
              ...pickerSelectStyles,
              iconContainer: {
                top: 10,
                right: 12,
              },
            }}
            useNativeAndroidPickerStyle={false}
            value={selectedTrialNumber2}
          />
        </VStack>
      </Box>
      {selectedTrialNumber1 && selectedTrialNumber2 ? (
        <>
          <Box style={styles.sectionContainer}>
            <Box onLayout={({nativeEvent}) => handleLayout({nativeEvent})}>
              {width ? (
                <LineChart
                  data={{
                    labels: Array.from(
                      {
                        length: Math.max(
                          ...[selectedTrialNumber1, selectedTrialNumber2].map(
                            trialNumber =>
                              lettuceGrowth.filter(
                                item => item.trial_number === trialNumber,
                              ).length,
                          ),
                        ),
                      },
                      (_, i) => `${i + 1}`,
                    ),
                    datasets: [
                      {
                        data: lettuceGrowth
                          .filter(
                            item => item.trial_number === selectedTrialNumber1,
                          )
                          .map(item => item.lettuce_height),
                      },
                      {
                        color: () => resolvedSecondary,
                        data: lettuceGrowth
                          .filter(
                            item => item.trial_number === selectedTrialNumber2,
                          )
                          .map(item => item.lettuce_height),
                      },
                    ],
                    legend: [
                      `Trial ${selectedTrialNumber1}`,
                      `Trial ${selectedTrialNumber2}`,
                    ],
                  }}
                  width={width}
                  height={220}
                  // formatXLabel={xValue => `Day ${xValue}`}
                  formatYLabel={yValue => `${parseFloat(yValue).toFixed(2)} cm`}
                  chartConfig={{
                    backgroundGradientFromOpacity: 0,
                    backgroundGradientToOpacity: 0,
                    color: () => resolvedPrimary,
                  }}
                />
              ) : null}
            </Box>
          </Box>
          <Box style={styles.sectionContainer}>
            <RNPickerSelect
              Icon={() => (
                <Icon
                  as={Ionicons}
                  name="chevron-down-outline"
                  size={resolvedSize}
                />
              )}
              items={Array.from(
                {
                  length: Math.max(
                    ...[selectedTrialNumber1, selectedTrialNumber2].map(
                      trialNumber =>
                        lettuceGrowth.filter(
                          item => item.trial_number === trialNumber,
                        ).length,
                    ),
                  ),
                },
                (_, i) => ({label: `${i + 1}`, value: `${i + 1}`}),
              )}
              onValueChange={value => setSelectedDayNumber(value)}
              placeholder={{label: 'Day Number'}}
              style={{
                ...pickerSelectStyles,
                iconContainer: {
                  top: 10,
                  right: 12,
                },
              }}
              useNativeAndroidPickerStyle={false}
              value={selectedDayNumber}
            />
          </Box>
          {selectedDayNumber ? (
            <>
              <Box style={styles.sectionContainer}>
                <VStack space="xs">
                  <HStack justifyContent="space-between">
                    <Text bold>Average % Difference</Text>
                    <Text>
                      {bar(
                        lettuceGrowth,
                        selectedTrialNumber1,
                        selectedTrialNumber2,
                        selectedDayNumber,
                      )}
                    </Text>
                  </HStack>
                  <HStack justifyContent="space-between">
                    <Text bold>% Difference at Day {selectedDayNumber}</Text>
                    <Text>
                      {foo(
                        lettuceGrowth,
                        selectedTrialNumber1,
                        selectedTrialNumber2,
                        selectedDayNumber,
                      )}
                    </Text>
                  </HStack>
                </VStack>
              </Box>
              {/*{downloadURLs ? (*/}
              {/*  <>*/}
              {/*    <Box style={styles.sectionContainer}>*/}
              {/*      <Box>*/}
              {/*        <Image*/}
              {/*          alt={downloadURLs[0]}*/}
              {/*          borderRadius="$xl"*/}
              {/*          height="$96"*/}
              {/*          source={{uri: downloadURLs[0]}}*/}
              {/*          width="$full"*/}
              {/*        />*/}
              {/*        <Badge*/}
              {/*          action="success"*/}
              {/*          alignSelf="start"*/}
              {/*          left="$4"*/}
              {/*          position="absolute"*/}
              {/*          top="$4">*/}
              {/*          <BadgeText>Online</BadgeText>*/}
              {/*          <BadgeIcon*/}
              {/*            as={Ionicons}*/}
              {/*            marginLeft="$2"*/}
              {/*            name="happy-outline"*/}
              {/*          />*/}
              {/*        </Badge>*/}
              {/*      </Box>*/}
              {/*    </Box>*/}
              {/*    <Box style={styles.sectionContainer} />*/}
              {/*  </>*/}
              {/*) : null}*/}
            </>
          ) : null}
        </>
      ) : null}
      <Box style={styles.sectionContainer}>
        <VStack space="xs">
          {lettuceHeight ? (
            <HStack justifyContent="space-between">
              <Text>Lettuce height</Text>
              <Text>{lettuceHeight}</Text>
            </HStack>
          ) : null}
          <Button onPress={async () => {
            try {
              const apps = firebase.apps;

              // await firebase.initializeApp(credentials, config);
              apps.forEach(app => {
                console.log('App name: ', app.name);
              });

              await firebase.app('SECONDARY_APP')

              try {
                await firebase.app('SECONDARY_APP').firestore()
                  .collection('Camera_Infos')
                  .doc('Android_cam')
                  .update({
                    request_img: 1,
                  })
                  .then(() => {
                    console.log('request_img updated!');
                  });
              } catch (error) {
                console.log('error!: ', error);
              }

              // console.log(await firebase.app('SECONDARY_APP').storage().ref('/public/128_36cm.jpg').getDownloadURL());
              await firebase.app('[DEFAULT]');
            } catch (error) {
              console.log('error: ', error);
            }
          }}>
            <ButtonText>Record lettuce height</ButtonText>
          </Button>
        </VStack>
      </Box>
    </ScrollViewContainer>
  );
}

function LettuceGrowthStackScreen(): React.JSX.Element {
  const colorMode = useColorMode();

  return (
    <LettuceGrowthStack.Navigator {...getStackNavigatorProps({colorMode})}>
      <LettuceGrowthStack.Screen
        component={LettuceGrowthScreen}
        name="LettuceGrowth"
        options={{headerTitle: 'Lettuce Growth'}}
      />
    </LettuceGrowthStack.Navigator>
  );
}

const PestDetectionStack = createStackNavigator();

function PestDetectionScreen(): React.JSX.Element {
  function getDownloadURLs(reference, pageToken) {
    let downloadURLs = [];

    function listFilesAndDirectories(reference, pageToken) {
      return reference.list({pageToken}).then(async result => {
        // // Loop over each item
        // result.items.forEach(ref => {
        //   console.log(ref.fullPath);
        // });

        const promise = await Promise.all(
          result.items.map(async (ref, index) => {
            return {
              fullPath: result.items[index].fullPath,
              downloadURL: await storage().ref(ref.fullPath).getDownloadURL(),
              trialNumber: `${index + 1}`,
              // trialNumber: result.items[index].fullPath.match(regex)[1],
              // lettuceHeight: parseFloat(
              //   result.items[index].fullPath.match(regex)[2],
              // ),
              // pestTotal: result.items[index].fullPath.match(regex)[3],
            };
          }),
        );

        downloadURLs.push(...promise);

        if (result.nextPageToken) {
          return listFilesAndDirectories(reference, result.nextPageToken);
        }

        console.log(`foo = ${JSON.stringify(downloadURLs)}`);

        return Promise.resolve(downloadURLs);
      });
    }

    return listFilesAndDirectories(reference, pageToken);
  }

  const reference = storage().ref('videos');

  const [downloadURLs, setDownloadURLs] = useState();

  const videoRef = useRef<VideoRef>(null);

  const [isPaused, setIsPaused] = useState(true);
  const [isMuted, setIsMuted] = useState(false);

  const resolvedRadius = useToken('radii', 'xl');

  const regex = /videos\/(\d+)_(\d+cm)_(\d+)\.mp4/;

  const resolvedSize = useToken('space', '6');

  const pickerSelectStyles = StyleSheet.create({
    inputIOS: {
      fontSize: 16,
      paddingVertical: 12,
      paddingHorizontal: 10,
      borderWidth: 1,
      borderColor: 'gray',
      borderRadius: 4,
      color: 'black',
      paddingRight: 30, // to ensure the text is never behind the icon
    },
    inputAndroid: {
      fontSize: 16,
      paddingHorizontal: 10,
      paddingVertical: 8,
      borderWidth: 0.5,
      // borderColor: 'purple',
      borderColor: 'gray',
      borderRadius: 8,
      color: 'black',
      paddingRight: 30, // to ensure the text is never behind the icon
    },
  });

  const [selectedTrialNumber, setSelectedTrialNumber] = useState();

  useEffect(() => {
    console.log('foo');
    getDownloadURLs(reference).then(value => {
      console.log(`value: ${JSON.stringify(value)}`);
      setDownloadURLs(value);
      if (selectedTrialNumber) {
        setUri(value[selectedTrialNumber - 1].downloadURL);
      }
    });
  }, [selectedTrialNumber]);

  const [uri, setUri] = useState();

  return (
    <ScrollViewContainer>
      {downloadURLs ? (
        <>
          {selectedTrialNumber && uri ? (
            <Box style={styles.sectionContainer}>
              <Box style={{position: 'relative', height: 256}}>
                <Video
                  controls={true}
                  key={uri}
                  muted={isMuted}
                  paused={isPaused}
                  ref={videoRef}
                  source={{uri: uri}}
                  style={[styles.backgroundVideo, {borderRadius: resolvedRadius}]}
                />
                {/*<Badge*/}
                {/*  action="success"*/}
                {/*  alignSelf="start"*/}
                {/*  position="absolute"*/}
                {/*  right="$4"*/}
                {/*  top="$4">*/}
                {/*  <BadgeText>Online</BadgeText>*/}
                {/*  <BadgeIcon as={Ionicons} marginLeft="$2" name="happy-outline" />*/}
                {/*</Badge>*/}
              </Box>
            </Box>
          ) : null}
          <Box style={styles.sectionContainer}>
            <RNPickerSelect
              Icon={() => (
                <Icon
                  as={Ionicons}
                  name="chevron-down-outline"
                  size={resolvedSize}
                />
              )}
              items={downloadURLs.map(element => ({
                key: element.trialNumber,
                label: element.trialNumber,
                value: element.trialNumber,
              }))}
              onValueChange={value => setSelectedTrialNumber(value)}
              placeholder={{label: 'Trial Number'}}
              style={{
                ...pickerSelectStyles,
                iconContainer: {
                  top: 10,
                  right: 12,
                },
              }}
              useNativeAndroidPickerStyle={false}
              value={selectedTrialNumber}
            />
          </Box>
          {selectedTrialNumber ? (
            <>
              {/*<Box style={styles.sectionContainer}>*/}
              {/*  <HStack justifyContent="space-between">*/}
              {/*    <Text bold>Lettuce height</Text>*/}
              {/*    <Text>{`${downloadURLs[selectedTrialNumber - 1].lettuceHeight.toFixed(2)} cm`}</Text>*/}
              {/*  </HStack>*/}
              {/*  <HStack justifyContent="space-between">*/}
              {/*    <Text bold>Average amount of pest</Text>*/}
              {/*    <Text>{downloadURLs[selectedTrialNumber - 1].pestTotal}</Text>*/}
              {/*  </HStack>*/}
              {/*</Box>*/}
              <Box style={styles.sectionContainer}>
                <VStack space="xs">
                  <Button onPress={() => setIsPaused(prevState => !prevState)}>
                    <ButtonText>{isPaused ? 'Play' : 'Pause'}</ButtonText>
                  </Button>
                  <Button onPress={() => setIsMuted(prevState => !prevState)}>
                    <ButtonText>{isMuted ? 'Unmute' : 'Mute'}</ButtonText>
                  </Button>
                </VStack>
              </Box>
            </>
          ) : null}
        </>
      ) : null}
    </ScrollViewContainer>
  );
}

function PestDetectionStackScreen(): React.JSX.Element {
  const colorMode = useColorMode();

  return (
    <PestDetectionStack.Navigator {...getStackNavigatorProps({colorMode})}>
      <PestDetectionStack.Screen
        component={PestDetectionScreen}
        name="PestDetection"
        options={{headerTitle: 'Pest Detection'}}
      />
    </PestDetectionStack.Navigator>
  );
}

const STYLES = ['default', 'dark-content', 'light-content'] as const;

const getTabBarProps = ({colorMode, route}) => {
  const isDarkMode = colorMode === 'dark';

  return {
    tabBarBackground: () => (
      <Box style={StyleSheet.absoluteFill}>
        <BlurView
          blurAmount={32}
          blurType={isDarkMode ? 'dark' : 'xlight'}
          overlayColor="#00000000"
          style={StyleSheet.absoluteFill}
        />
      </Box>
    ),
    tabBarIcon: ({focused, color, size}) => {
      let iconName;

      // if (route.name === 'HomeStack') {
      //   iconName = focused ? 'home' : 'home-outline';
      // }
      if (route.name === 'SensorsStack') {
        iconName = focused ? 'bar-chart' : 'bar-chart-outline';
      } else if (route.name === 'LettuceGrowthStack') {
        iconName = focused ? 'leaf' : 'leaf-outline';
      } else if (route.name === 'PestDetectionStack') {
        iconName = focused ? 'bug' : 'bug-outline';
      }

      return <Icon as={Ionicons} color={color} name={iconName} size={size} />;
    },
    tabBarStyle: {position: 'absolute'},
  };
};

const getTabNavigatorProps = ({colorMode}) => {
  return {
    screenOptions: ({route}) => ({
      ...getTabBarProps({colorMode, route}),
      headerShown: false,
    }),
  };
};

const BottomTab = createBottomTabNavigator();

function HomeTabs(): React.JSX.Element {
  const colorMode = useColorMode();
  const isDarkMode = colorMode === 'dark';

  const statusBarStyle = isDarkMode ? STYLES[2] : STYLES[1];

  return (
    <>
      <StatusBar barStyle={statusBarStyle} />
      <BottomTab.Navigator {...getTabNavigatorProps({colorMode})}>
        {/*<BottomTab.Screen*/}
        {/*  component={HomeStackScreen}*/}
        {/*  name="HomeStack"*/}
        {/*  options={{tabBarLabel: 'Home'}}*/}
        {/*/>*/}
        <BottomTab.Screen
          component={SensorsStackScreen}
          name="SensorsStack"
          options={{tabBarLabel: 'Sensors'}}
        />
        <BottomTab.Screen
          component={LettuceGrowthStackScreen}
          name="LettuceGrowthStack"
          options={{tabBarLabel: 'Lettuce Growth'}}
        />
        <BottomTab.Screen
          component={PestDetectionStackScreen}
          name="PestDetectionStack"
          options={{tabBarLabel: 'Pest Detection'}}
        />
      </BottomTab.Navigator>
    </>
  );
}

const RootStack = createStackNavigator();

function Foo(): React.JSX.Element {
  const colorMode = useColorMode();
  const isDarkMode = colorMode === 'dark';

  return (
    <NavigationContainer theme={isDarkMode ? DarkTheme : DefaultTheme}>
      <RootStack.Navigator screenOptions={() => ({headerShown: false})}>
        <RootStack.Screen component={HomeTabs} name="HomeTabs" />
      </RootStack.Navigator>
    </NavigationContainer>
  );
}

function App(): React.JSX.Element {
  return (
    // <Provider store={store}>
    //   <GluestackUIProvider config={config}>
    //     <Foo />
    //   </GluestackUIProvider>
    // </Provider>
    <GluestackUIProvider config={config}>
      <Foo />
    </GluestackUIProvider>
  );
}

export default App;
// /**
//  * Sample React Native App
//  * https://github.com/facebook/react-native
//  *
//  * @format
//  */
//
// import React from 'react';
// import type {PropsWithChildren} from 'react';
// import {
//   SafeAreaView,
//   ScrollView,
//   StatusBar,
//   StyleSheet,
//   Text,
//   useColorScheme,
//   View,
// } from 'react-native';
//
// import {
//   Colors,
//   DebugInstructions,
//   Header,
//   LearnMoreLinks,
//   ReloadInstructions,
// } from 'react-native/Libraries/NewAppScreen';
//
// type SectionProps = PropsWithChildren<{
//   title: string;
// }>;
//
// function Section({children, title}: SectionProps): React.JSX.Element {
//   const isDarkMode = useColorScheme() === 'dark';
//   return (
//     <View style={styles.sectionContainer}>
//       <Text
//         style={[
//           styles.sectionTitle,
//           {
//             color: isDarkMode ? Colors.white : Colors.black,
//           },
//         ]}>
//         {title}
//       </Text>
//       <Text
//         style={[
//           styles.sectionDescription,
//           {
//             color: isDarkMode ? Colors.light : Colors.dark,
//           },
//         ]}>
//         {children}
//       </Text>
//     </View>
//   );
// }
//
// function App(): React.JSX.Element {
//   const isDarkMode = useColorScheme() === 'dark';
//
//   const backgroundStyle = {
//     backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
//   };
//
//   return (
//     <SafeAreaView style={backgroundStyle}>
//       <StatusBar
//         barStyle={isDarkMode ? 'light-content' : 'dark-content'}
//         backgroundColor={backgroundStyle.backgroundColor}
//       />
//       <ScrollView
//         contentInsetAdjustmentBehavior="automatic"
//         style={backgroundStyle}>
//         <Header />
//         <View
//           style={{
//             backgroundColor: isDarkMode ? Colors.black : Colors.white,
//           }}>
//           <Section title="Step One">
//             Edit <Text style={styles.highlight}>App.tsx</Text> to change this
//             screen and then come back to see your edits.
//           </Section>
//           <Section title="See Your Changes">
//             <ReloadInstructions />
//           </Section>
//           <Section title="Debug">
//             <DebugInstructions />
//           </Section>
//           <Section title="Learn More">
//             Read the docs to discover what to do next:
//           </Section>
//           <LearnMoreLinks />
//         </View>
//       </ScrollView>
//     </SafeAreaView>
//   );
// }
//
// const styles = StyleSheet.create({
//   sectionContainer: {
//     marginTop: 32,
//     paddingHorizontal: 24,
//   },
//   sectionTitle: {
//     fontSize: 24,
//     fontWeight: '600',
//   },
//   sectionDescription: {
//     marginTop: 8,
//     fontSize: 18,
//     fontWeight: '400',
//   },
//   highlight: {
//     fontWeight: '700',
//   },
// });
//
// export default App;
