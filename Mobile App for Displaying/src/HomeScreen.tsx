import React from 'react';
import {
  Box,
  Button,
  ButtonText,
  Input,
  InputField,
  Text,
  VStack,
} from '@gluestack-ui/themed';
import styles from './styles.ts';
import ImagePicker from 'react-native-image-crop-picker';
import storage from '@react-native-firebase/storage';
import ScrollViewContainer from './ScrollViewContainer.tsx';
import {useNavigation} from '@react-navigation/native';

function HomeScreen(): React.JSX.Element {
  function listFilesAndDirectories(reference, pageToken) {
    return reference.list({pageToken}).then(result => {
      // Loop over each item
      result.items.forEach(ref => {
        console.log(ref.fullPath);
      });

      if (result.nextPageToken) {
        return listFilesAndDirectories(reference, result.nextPageToken);
      }

      return Promise.resolve();
    });
  }

  const reference = storage().ref('images');

  const navigation = useNavigation();

  return (
    <ScrollViewContainer>
      <Box style={styles.sectionContainer}>
        <VStack space="xl">
          <VStack space="xs">
            <Text lineHeight="$xs">Storage</Text>
            <Button
              action="primary"
              isDisabled={false}
              isFocusVisible={false}
              onPress={async () => {
                ImagePicker.openCamera({}).then(image => {
                  console.log(image);
                });
              }}
              size="md"
              variant="solid">
              <ButtonText>Open camera</ButtonText>
            </Button>
            <Button
              action="primary"
              isDisabled={false}
              isFocusVisible={false}
              onPress={async () => {
                ImagePicker.openPicker({}).then(async image => {
                  console.log(image.sourceURL);
                  // create bucket storage reference to not yet existing image
                  const reference = storage().ref(`images/${image.filename}`);
                  // path to existing file on filesystem
                  const pathToFile = image.sourceURL;
                  // uploads file
                  await reference.putFile(pathToFile);
                });
              }}
              size="md"
              variant="solid">
              <ButtonText>Upload an image</ButtonText>
            </Button>
            <Button
              action="primary"
              isDisabled={false}
              isFocusVisible={false}
              onPress={() => {
                // listFilesAndDirectories(reference).then(() => {
                //   console.log('Finished listing');
                // });
                navigation.navigate('Files');
              }}
              size="md"
              variant="solid">
              <ButtonText>List files</ButtonText>
            </Button>
          </VStack>

          <VStack space="xs">
            <Text>Firestore Database</Text>
            <Button
              action="primary"
              isDisabled={false}
              isFocusVisible={false}
              onPress={() => console.log('Hello, World!')}
              size="md"
              variant="solid">
              <ButtonText>Read data</ButtonText>
            </Button>
            <Input
              variant="outline"
              size="md"
              isDisabled={false}
              isInvalid={false}
              isReadOnly={false}>
              <InputField placeholder="Name" type="text" />
            </Input>
            <Input
              variant="outline"
              size="md"
              isDisabled={false}
              isInvalid={false}
              isReadOnly={false}>
              <InputField placeholder="Age" type="text" />
            </Input>
            <Button
              action="primary"
              isDisabled={false}
              isFocusVisible={false}
              onPress={() => console.log('Hello, World!')}
              size="md"
              variant="solid">
              <ButtonText>Add data</ButtonText>
            </Button>
          </VStack>

          <VStack space="xs">
            <Text>Realtime Database</Text>
          </VStack>
        </VStack>
      </Box>
    </ScrollViewContainer>
  );
}

export default HomeScreen;
